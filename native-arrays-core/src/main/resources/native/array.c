// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#include "jobject_hashmap.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#define DEBUG 1

#define ADDRESS_FIELD "address"
#define SIZE_FIELD "size"

#define CONSTRUCTOR_METHOD "<init>"

#define STRING_H(X) #X
#define STRING(X) STRING_H(X)

#define JAVA_EXT_PACKAGE "me/theeninja/nativearrays/core/"
#define JAVA_INC_PACKAGE "java/util/function"

#define SUPER_ARRAY_CLASS_NAME JAVA_PACKAGE "Array"
#define ARRAY_CLASS_NAME JAVA_PACKAGE STRING(TYPE) "Array"

#if SPECIALIZED_JAVA_CONSUMER
#define CONSUMER_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "Consumer"
#define PREDICATE_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "Predicate"
#define UNARY_OPERATOR_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "UnaryOperator"
#define BINARY_OPERATOR_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "BinaryOperator"
#else
#define CONSUMER_CLASS_NAME JAVA_EXT_PACKAGE STRING(TYPE) "Consumer"
#define PREDICATE_CLASS_NAME JAVA_EXT_PACKAGE STRING(TYPE) "Predicate"
#define UNARY_OPERATOR_CLASS_NAME JAVA_EXT_PACKAGE STRING(TYPE) "UnaryOperator"
#define BINARY_OPERATOR_CLASS_NAME JAVA_EXT_PACKAGE STRING(TYPE) "BinaryOperator"
#endif

#define INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME JAVA_EXT_PACKAGE "Index" STRING(TYPE) "PairConsumer"
#define COMPARATOR_CLASS_NAME JAVA_EXT_PACKAGE STRING(TYPE) "Comparator"

#define CONSUMER_APPLY "accept"
#define PREDICATE_TEST "test"
#define UNARY_OPERATOR_APPLY_AS_TYPE "applyAs" STRING(TYPE)
#define BINARY_OPERATOR_APPLY_AS_TYPE UNARY_OPERATOR_APPLY_AS_TYPE
#define COMPARATOR_COMPARE "compare"

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
jmethodID comparatorCompare;
jmethodID predicateTest;
jmethodID unaryOperatorApplyAsType;
jmethodID binaryOperatorApplyAsType;

struct hashmap* hashMap;

#define JNI_VERSION JNI_VERSION_10

void check(JNIEnv* env) {
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionDescribe(env);
    }
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    void** envAddress = (void **) &env;

    if ((*vm)->GetEnv(vm, envAddress, JNI_VERSION) != JNI_OK) {
        return JNI_ERR;
    }

    jclass localArraySuperClass = (*env)->FindClass(env, SUPER_ARRAY_CLASS_NAME);
    jclass localArrayClass = (*env)->FindClass(env, ARRAY_CLASS_NAME);

    arrayClass = (*env)->NewGlobalRef(env, localArrayClass);

    addressField = (*env)->GetFieldID(env, localArraySuperClass, ADDRESS_FIELD, LONG);
    sizeField = (*env)->GetFieldID(env, localArraySuperClass, SIZE_FIELD, LONG);

    arrayConstructor = (*env)->GetMethodID(env, arrayClass, CONSTRUCTOR_METHOD, ARRAY_CONSTRUCTOR_SIGNATURE);

    jclass localConsumerClass = (*env)->FindClass(env, CONSUMER_CLASS_NAME);
    consumerApply = (*env)->GetMethodID(env, localConsumerClass, CONSUMER_APPLY, CONSUMER_APPLY_SIGNATURE);

    jclass localIndexValuePairConsumerClass = (*env)->FindClass(env, INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME);
    indexValuePairConsumerApply = (*env)->GetMethodID(env, localIndexValuePairConsumerClass, CONSUMER_APPLY, INDEX_VALUE_PAIR_CONSUMER_APPLY_SIGNATURE);

    jclass localComparatorClass = (*env)->FindClass(env, COMPARATOR_CLASS_NAME);
    comparatorCompare = (*env)->GetMethodID(env, localComparatorClass, COMPARATOR_COMPARE);

    jclass localPredicateClass = (*env)->FindClass(env, PREDICATE_CLASS_NAME);
    predicateTest = (*env)->GetMethodID(env, localPredicateClass, PREDICATE_TEST);

    jclass localUnaryOperatorClass = (*env)->FindClass(env, UNARY_OPERATOR_CLASS_NAME);
    unaryOperatorApplyAsType = (*env)->GetMethodID(env, localUnaryOperatorClass, UNARY_OPERATOR_APPLY_AS_TYPE);

    jclass localBinaryOperatorClass = (*env)->FindClass(env, BINARY_OPERATOR_APPLY_AS_TYPE);
    binaryOperatorApplyAsType = (*env)->GetMethodID(env, localBinaryOperatorClass, BINARY_OPERATOR_APPLY_AS_TYPE);

    hashMap = hashmap_new();

    return JNI_VERSION;
}

uint_fast64_t getSize(JNIEnv* env, jobject instance) {
    //struct hashmap_node* jObjectNode = hashmap_get(hashMap, &instance);

    // return jObjectNode->size;

    return (uint_fast64_t) (*env)->GetLongField(env, instance, sizeField);
}

JAVA_TYPE* getAddress(JNIEnv* env, jobject instance) {
    //struct hashmap_node* jObjectNode = hashmap_get(hashMap, &instance);

    // return jObjectNode->address;

    return (JAVA_TYPE*) (*env)->GetLongField(env, instance, addressField);
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

    if (size > 1LL << (8 * sizeof(JAVA_TYPE) - 1)) {
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

JNIEXPORT jlong JNICALL JNI_METHOD(malloc)(JNIEnv* env, jobject arrayInstance) {
    printf("Malloc 1\n");
    printf("Malloc 2\n");

    uint_fast64_t size = getSize(env, arrayInstance);

    printf("Size %lld\n", size);

    JAVA_TYPE* address = malloc(sizeof(JAVA_TYPE) * size);
    printf("Malloc 3\n");

    printf("Allocated address %p\n", address);

  // hashmap_put(hashMap, &arrayInstance, address, size);

    return (jlong) address;
}

JNIEXPORT jobject JNICALL JNI_METHOD(fromJavaArray)(JNIEnv* env, jclass arrayClass, ARRAY(JAVA_TYPE) javaArray) {
    printf("from 0\n");
    jsize size = (*env)->GetArrayLength(env, javaArray);
    printf("from 1\n");
    jobject instance = (*env)->NewObject(env, arrayClass, arrayConstructor, size);

    printf("from 2\n");

    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);
        printf("from 3\n");

    JAVA_TYPE* address = getAddress(env, instance);

    printf("Holla\n");
    printf("%p\n", address);
    printf("from 4\n");

    #if DEBUG
        for (jlong i = 0; i < size; i++) {
            printf("1\n");
            // printf(JAVA_TYPE_FORMAT_SPECIFIER "\n", address[i]);
        }
    #endif

    printf("out of loop\n");

    memcpy(address, javaArrayValues, sizeof(JAVA_TYPE) * size);
    printf("from 5\n");

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaArrayValues, 0);
    printf("from 6\n");

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

#define MAX_UNSIGNED_VALUE(TYPE) (1LL << (sizeof(TYPE) * 8 - 1))

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
    uint_fast64_t size = getSize(env, instance);

    const JAVA_TYPE requiredLength = GET_REQUIRED_LENGTH(size); // maximum size of jstring is JAVA_TYPE

    // + 1 is for the null byte
    char* string = malloc(sizeof(char) * requiredLength + 1);

    JAVA_TYPE* address = getAddress(env, instance);

    strcat(string, ARRAY_START);

    for (uint_fast64_t index = 0; index < size; index++) {
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
    printf("a\n");
    uint_fast64_t size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);
    printf("b\n");
    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];
        printf("c\n");
        (*env)->CallVoidMethod(env, indexValuePairConsumer, indexValuePairConsumerApply, index, value);
    }
    printf("d\n");
}

#define CALL_TYPE_METHOD Call##Type##Method

JNIEXPORT jobject JNICALL JNI_METHOD(map)(JNIEnv* env, jobject oldArray, jobject mapper) {
    const uint_fast64_t size = getSize(env, oldArray);
    JAVA_TYPE* oldAddress = getAddress(env, oldArray);

    jobject newArray = (*env)->NewObject(env, arrayClass, size);
    JAVA_TYPE* newAddress = getAddress(env, newArray);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = oldAddress[index];
        const JAVA_TYPE newValue = (*env)->CALL_TYPE_METHOD(env, mapper, unaryOperatorApplyAsType, oldValue);

        newAddress[index] = newValue;
    }

    return newArray;
}

JNIEXPORT void JNICALL JNI_METHOD(mapLocally)(JNIEnv* env, jobject array, jobject mapper) {
    uint_fast64_t size = getSize(env, array);
    JAVA_TYPE* address = getAddress(env, array);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = address[index];
        const JAVA_TYPE newValue = (*env)->CALL_TYPE_METHOD(env, mapper, unaryOperatorApplyAsType, oldValue);

        address[index] = newValue;
    }
}

JNIEXPORT jobject JNICALL JNI_METHOD(filter)(JNIEnv* jobject array, jobject filterer) {

}


JNIEXPORT jobject JNICALL JNI_METHOD(filterLocally)(JNIEnv* jobject array, jobject filterer) {

}

JNIEXPORT void JNICALL JNI_METHOD(forEachValue)(JNIEnv* env, jobject instance, jobject consumer) {
    uint_fast64_t size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        (*env)->CallVoidMethod(env, consumer, indexValuePairConsumerApply, value);
    }
}

JNIEXPORT jobject JNICALL JNI_METHOD(copy)(JNIEnv* env, jobject instance) {
    uint_fast64_t size = getSize(env, instance);

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
    uint_fast64_t size = getSize(env, instance);

    for (uint_fast64_t index = 0; index < size; index++) {
        const jlong value = address[index];

        if (requestedValue == value) {
            return index;
        }
    }

    return NOT_FOUND;
}

JNIEXPORT jlong JNICALL JNI_METHOD(searchBackwards)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {

    JAVA_TYPE* address = getAddress(env, instance);
    uint_fast64_t size = getSize(env, instance);

    for (uint_fast64_t index = size - 1; index >= 0; index--) {
        const jlong value = address[index];

        if (requestedValue == value) {
            return index;
        }
    }

    return NOT_FOUND;
}

JNIEXPORT jlong JNICALL JNI_METHOD(count)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {
    JAVA_TYPE* address = getAddress(env, instance);
    uint_fast64_t size = getSize(env, instance);
    uint_fast64_t count = 0;

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (requestedValue == value) {
            count++;
        }
    }

    return (jlong) count;
}