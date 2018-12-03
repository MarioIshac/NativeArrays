// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#include "jobject_hashmap.h"
#include "array.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define ADDRESS_FIELD "address"
#define SIZE_FIELD "size"

#define CONSTRUCTOR_METHOD "<init>"

#define STRING_H(X) #X
#define STRING(X) STRING_H(X)

#define JAVA_PACKAGE "me/theeninja/nativearrays/core/"

#define ARRAY_CLASS_NAME JAVA_PACKAGE STRING(TYPE) "Array"
#define CONSUMER_CLASS_NAME "java/util/function/" STRING(TYPE) "Consumer"
#define INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME JAVA_PACKAGE "Index" STRING(TYPE) "PairConsumer"

#define CONSUMER_APPLY "accept"

#define VOID "V"
#define LONG "J"

#define ARRAY_CONSTRUCTOR_SIGNATURE "(" LONG ")" VOID
#define CONSUMER_APPLY_SIGNATURE "(" SHORT_TYPE ")" VOID
#define INDEX_VALUE_PAIR_CONSUMER_APPLY_SIGNATURE "(" LONG SHORT_TYPE ")V"

#define MER_H(A, B, C, D) A##B##C##D
#define MER(A, B, C, D) MER_H(A, B, C, D)
#define JNI_METHOD(NAME) MER(Java_me_theeninja_nativearrays_core_, TYPE, Array_, NAME)

#define NOT_FOUND -1

/**
 * Before including this class, the four macros must be defined:
 *
 * JAVA_TYPE (representing what token in C correlates to the java type)
 * TYPE (upper-case representation of the type as specified in java)
 * SHORT_TYPE (representation of type in metod signature)
 * JAVA_TYPE_FORMAT_SPECIFIER (format specifier for the java type (accounting for differences between C and Java types of the same name)
 */

jclass arrayClass;
jfieldID addressField;
jfieldID sizeField;
jmethodID arrayConstructor;
jmethodID consumerApply;
jmethodID indexValuePairConsumerApply;

struct hashmap* hashMap;

#define JNI_VERSION JNI_VERSION_10

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    void** envAddress = (void **) &env;

    if ((*vm)->GetEnv(vm, envAddress, JNI_VERSION) != JNI_OK) {
        return JNI_ERR;
    }

    jclass localArrayClass = (*env)->FindClass(env, ARRAY_CLASS_NAME);
    arrayClass = (*env)->NewGlobalRef(env, localArrayClass);
    addressField = (*env)->GetFieldID(env, arrayClass, ADDRESS_FIELD, LONG);
    sizeField = (*env)->GetFieldID(env, arrayClass, SIZE_FIELD, LONG);

    arrayConstructor = (*env)->GetMethodID(env, arrayClass, CONSTRUCTOR_METHOD, ARRAY_CONSTRUCTOR_SIGNATURE);

    jclass localConsumerClass = (*env)->FindClass(env, CONSUMER_CLASS_NAME);

    consumerApply = (*env)->GetMethodID(env, localConsumerClass, CONSUMER_APPLY, CONSUMER_APPLY_SIGNATURE);

    jclass localIndexValuePairConsumerClass = (*env)->FindClass(env, INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME);

    indexValuePairConsumerApply = (*env)->GetMethodID(env, localIndexValuePairConsumerClass, CONSUMER_APPLY, INDEX_VALUE_PAIR_CONSUMER_APPLY_SIGNATURE);

    hashMap = hashmap_new();

    return JNI_VERSION;
}

JAVA_TYPE* getAddress(JNIEnv* env, jobject instance) {
    struct hashmap_node* jObjectNode = hashmap_get(hashMap, &instance);

    return jObjectNode->address;
}

jlong getSize(JNIEnv* env, jobject instance) {
    struct hashmap_node* jObjectNode = hashmap_get(hashMap, &instance);

    return jObjectNode->size;
}


void JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    void** envAddress = (void **) &env;

    if ((*vm)->GetEnv(vm, envAddress, JNI_VERSION) != JNI_OK) {
        return;
    }

    hashmap_free(env, hashMap);

    (*env)->DeleteGlobalRef(env, arrayClass);
}

JNIEXPORT JAVA_TYPE JNICALL JNI_METHOD(get)(JNIEnv* env, jobject instance, jlong index) {
    JAVA_TYPE* address = getAddress(env, instance);

    const JAVA_TYPE jValue = address[index];

    return jValue;
}

JNIEXPORT void JNICALL JNI_METHOD(set)(JNIEnv* env, jobject instance, jlong index, JAVA_TYPE value) {
    JAVA_TYPE* address = getAddress(env, instance);

    address[index] = value;
}

JNIEXPORT void JNICALL JNI_METHOD(close)(JNIEnv* env, jobject instance) {
    hashmap_remove(env, hashMap, &instance);
}

#define ARRAY_H(JAVA_TYPE) JAVA_TYPE##Array
#define ARRAY(JAVA_TYPE) ARRAY_H(JAVA_TYPE)

#define NEW_ARRAY_H(TYPE) New##TYPE##Array
#define NEW_ARRAY(TYPE) NEW_ARRAY_H(TYPE)

#define GET_ARRAY_ELEMENTS_H(TYPE) Get##TYPE##ArrayElements
#define GET_ARRAY_ELEMENTS(TYPE) GET_ARRAY_ELEMENTS_H(TYPE)

#define RELEASE_ARRAY_ELEMENTS_H(TYPE) Release##TYPE##ArrayElements
#define RELEASE_ARRAY_ELEMENTS(TYPE) RELEASE_ARRAY_ELEMENTS_H(TYPE)

JNIEXPORT ARRAY(JAVA_TYPE) JNICALL JNI_METHOD(toJavaArray)(JNIEnv* env, jobject instance) {
    jlong size = getSize(env, instance);

    if (size > 1 << (8 * sizeof(JAVA_TYPE) - 1)) {
        return NULL;
    }

    ARRAY(JAVA_TYPE) javaArray = (*env)->NEW_ARRAY(TYPE)(env, size);
    JAVA_TYPE* javaValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);

    if (javaValues == NULL) {
        return NULL;
    }

    JAVA_TYPE* address = getAddress(env, instance);

    memcpy(javaValues, address, size * sizeof(JAVA_TYPE));

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaValues, 0);

    return javaArray;
}

JNIEXPORT void JNICALL JNI_METHOD(intoJavaArray)(JNIEnv* env, jobject instance, ARRAY(JAVA_TYPE) javaArray) {
    jsize javaValuesLength = (*env)->GetArrayLength(env, javaArray);
    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);

    JAVA_TYPE* address = getAddress(env, instance);

    memcpy(javaArrayValues, address, javaValuesLength * sizeof(JAVA_TYPE));

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaArrayValues, 0);
}

JNIEXPORT jobject JNICALL JNI_METHOD(create)(JNIEnv* env, jclass arrayClass, jlong size) {
  JAVA_TYPE* address = malloc(sizeof(JAVA_TYPE) * size);

  jobject localInstance = (*env)->NewObject(env, arrayClass, arrayConstructor, size);
  jobject globalInstance = (*env)->NewGlobalRef(env, localInstance);

  hashmap_put(hashMap, &globalInstance, address, size);

  return globalInstance;
}

JNIEXPORT jobject JNICALL JNI_METHOD(fromJavaArray)(JNIEnv* env, jclass arrayClass, ARRAY(JAVA_TYPE) javaArray) {
    jsize size = (*env)->GetArrayLength(env, javaArray);

    jobject instance = JNI_METHOD(create)(env, arrayClass, size);

    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);
    JAVA_TYPE* address = getAddress(env, instance);

    memcpy(address, javaArrayValues, sizeof(JAVA_TYPE) * size);

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaArrayValues, 0);

    return instance;
}

JNIEXPORT jboolean JNI_METHOD(equals)(JNIEnv* env, jobject instance, jobject other) {
    if (other == NULL) {
        return JNI_FALSE;
    }

    jclass otherClass = (*env)->GetObjectClass(env, other);

    if (arrayClass != otherClass) {
        return JNI_FALSE;
    }

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    return address == otherAddress;
}

#define MAX_UNSIGNED_VALUE(TYPE) (1 << (sizeof(TYPE) * 8 - 1))

JNIEXPORT jint JNICALL JNI_METHOD(hashCode)(JNIEnv* env, jobject instance) {
    uintptr_t address = (uintptr_t) getAddress(env, instance);

    // number of possible addresses is greater than number of possible hashcodes, so restrict domain
    address %= MAX_UNSIGNED_VALUE(jint);

    // jint is signed, so subtract half the unsigned value

    address -= (MAX_UNSIGNED_VALUE(jint) / 2);

    return (jint) address;
}

#define ARRAY_START "["
#define ARRAY_END "]"
#define ARRAY_SEPARATOR ", "

#define MAX_JAVA_TYPE_LENGTH 10
#define BRACKETS_LENGTH 2
#define GET_SEPARATOR_LENGTH(arrayLength) (((arrayLength) - 1) * 2)
#define NULL_TERMINATOR_LENGTH 1

#define GET_REQUIRED_LENGTH(arrayLength) ((arrayLength) * (MAX_JAVA_TYPE_LENGTH) + (GET_SEPARATOR_LENGTH(arrayLength)) + (BRACKETS_LENGTH) + (NULL_TERMINATOR_LENGTH))

JNIEXPORT jstring JNICALL JNI_METHOD(toString)(JNIEnv* env, jobject instance) {
    jlong size = getSize(env, instance);

    const JAVA_TYPE requiredLength = GET_REQUIRED_LENGTH(size); // maximum size of jstring is JAVA_TYPE

    // + 1 is for the null byte
    char* string = malloc(sizeof(char) * requiredLength + 1);

    JAVA_TYPE* address = getAddress(env, instance);

    strcat(string, ARRAY_START);

    for (jlong index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        // + 1 is for the null byte
        char valueString[MAX_JAVA_TYPE_LENGTH + 1];

        sprintf(valueString, JAVA_TYPE_FORMAT_SPECIFIER, value);

        strcat(string, valueString);

        if (index == size - 1) {
            strcat(string, ARRAY_SEPARATOR);
        }
    }

    strcat(string, ARRAY_END);

    jstring jString = (*env)->NewStringUTF(env, string);

    return jString;
}

#define CONSUME_METHOD_NAME "apply"

JNIEXPORT void JNICALL JNI_METHOD(forEachIndexValuePair)(JNIEnv* env, jobject instance, jobject indexValuePairConsumer) {
    jlong size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    for (jlong index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        (*env)->CallVoidMethod(env, indexValuePairConsumer, indexValuePairConsumerApply, index, value);
    }
}

JNIEXPORT void JNICALL JNI_METHOD(forEachValue)(JNIEnv* env, jobject instance, jobject consumer) {
    jlong size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    for (jlong index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        (*env)->CallVoidMethod(env, consumer, indexValuePairConsumerApply, value);
    }
}

JNIEXPORT jobject JNICALL JNI_METHOD(copy)(JNIEnv* env, jobject instance) {
    jlong size = getSize(env, instance);

    jobject other = (*env)->NewObject(env, arrayClass, arrayConstructor, size);

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    memcpy(otherAddress, address, sizeof(JAVA_TYPE) * size);

    return other;
}

JNIEXPORT void JNICALL JNI_METHOD(fill)(JNIEnv* env, jobject instance, JAVA_TYPE value) {
    jlong size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    memset(address, value, sizeof(JAVA_TYPE) * size);
}

JNIEXPORT jlong JNICALL JNI_METHOD(searchForwards)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {

    JAVA_TYPE* address = getAddress(env, instance);
    jlong size = getSize(env, instance);

    for (jlong index = 0; index < size; index++) {
        const jlong value = address[index];

        if (requestedValue == value) {
            return index;
        }
    }

    return NOT_FOUND;
}

JNIEXPORT jlong JNICALL JNI_METHOD(searchBackwards)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {

    JAVA_TYPE* address = getAddress(env, instance);
    jlong size = getSize(env, instance);

    for (jlong index = size - 1; index >= 0; index--) {
        const jlong value = address[index];

        if (requestedValue == value) {
            return index;
        }
    }

    return NOT_FOUND;
}

JNIEXPORT jlong JNICALL JNI_METHOD(count)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {
    JAVA_TYPE* address = getAddress(env, instance);
    jlong size = getSize(env, instance);
    jlong count = 0;

    for (jlong index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (requestedValue == value) {
            count++;
        }
    }

    return count;
}