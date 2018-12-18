// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include <jni.h>

#define DEBUG 1

#define ADDRESS_FIELD "address"
#define SIZE_FIELD "size"
#define EXCLUDED_VALUE_FIELD "excludedValue"

#define CONSTRUCTOR_METHOD "<init>"

#define STRING_H(X) #X
#define STRING(X) STRING_H(X)

#define JAVA_EXT_PACKAGE "me/theeninja/nativearrays/core/"
#define ARRAY_SUBPACKAGE "array/"
#define FILTERED_ARRAY_SUBPACKAGE ARRAY_SUBPACKAGE "filtered/"
#define UNFILTERED_ARRAY_SUBPACKAGE ARRAY_SUBPACKAGE "unfiltered/"
#define JAVA_INC_PACKAGE "java/util/function/"

#define ARRAY_CLASS_NAME_SUFFIX "Array"
#define ARRAY_SUBPACKAGE "array/"

#define SUPER_ARRAY_CLASS_NAME JAVA_EXT_PACKAGE ARRAY_SUBPACKAGE ARRAY_CLASS_NAME_SUFFIX
#define ARRAY_CLASS_NAME JAVA_EXT_PACKAGE ARRAY_SUBPACKAGE STRING(TYPE) ARRAY_CLASS_NAME_SUFFIX
#define FILTERED_ARRAY_CLASS_NAME JAVA_EXT_PACKAGE FILTERED_ARRAY_SUBPACKAGE "Filtered" STRING(TYPE) ARRAY_CLASS_NAME_SUFFIX
#define UNFILTERED_ARRAY_CLASS_NAME JAVA_EXT_PACKAGE UNFILTERED_ARRAY_SUBPACKAGE "Unfiltered" STRING(TYPE) ARRAY_CLASS_NAME_SUFFIX

#define CONSUMER_SUBPACKAGE "consumers/"
#define CONSUMER_PAIR_SUBPACKAGE CONSUMER_SUBPACKAGE "pair/"
#define CONSUMER_VALUE_SUBPACKAGE CONSUMER_SUBPACKAGE "value/"

#define OPERATOR_SUBPACKAGE "operators/"
#define UNARY_OPERATOR_SUBPACKAGE OPERATOR_SUBPACKAGE "unary/"
#define BINARY_OPERATOR_SUBPACKAGE OPERATOR_SUBPACKAGE "binary/"

#define PREDICATES_SUBPACKAGE "predicates/"
#define COMPARATOR_SUBPACKAGE "comparator/"

#if SPECIALIZED_JAVA_CONSUMER
#define CONSUMER_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "Consumer"
#define PREDICATE_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "Predicate"
#define UNARY_OPERATOR_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "UnaryOperator"
#define BINARY_OPERATOR_CLASS_NAME JAVA_INC_PACKAGE STRING(TYPE) "BinaryOperator"
#else
#define CONSUMER_CLASS_NAME JAVA_EXT_PACKAGE CONSUMER_VALUE_SUBPACKAGE STRING(TYPE) "Consumer"
#define PREDICATE_CLASS_NAME JAVA_EXT_PACKAGE PREDICATES_SUBPACKAGE STRING(TYPE) "Predicate"
#define UNARY_OPERATOR_CLASS_NAME JAVA_EXT_PACKAGE UNARY_OPERATOR_SUBPACKAGE STRING(TYPE) "UnaryOperator"
#define BINARY_OPERATOR_CLASS_NAME JAVA_EXT_PACKAGE BINARY_OPERATOR_SUBPACKAGE STRING(TYPE) "BinaryOperator"
#endif

#define A printf("%d\n", __LINE__);

#define INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME JAVA_EXT_PACKAGE CONSUMER_PAIR_SUBPACKAGE "Index" STRING(TYPE) "PairConsumer"
#define COMPARATOR_CLASS_NAME JAVA_EXT_PACKAGE COMPARATOR_SUBPACKAGE STRING(TYPE) "Comparator"

#define CONSUMER_APPLY "accept"
#define PREDICATE_TEST "test"
#define UNARY_OPERATOR_APPLY_AS_TYPE "applyAs" STRING(TYPE)
#define BINARY_OPERATOR_APPLY_AS_TYPE UNARY_OPERATOR_APPLY_AS_TYPE
#define COMPARATOR_COMPARE "compare"

#define VOID "V"
#define LONG "J"
#define BOOLEAN "Z"

#define FILTERED_ARRAY_CONSTRUCTOR_SIGNATURE "(" LONG LONG SHORT_TYPE ")" VOID
#define UNFILTERED_ARRAY_CONSTRUCTOR_SIGNATURE "(" LONG ")" VOID
#define CONSUMER_APPLY_SIGNATURE "(" SHORT_TYPE ")" VOID
#define INDEX_VALUE_PAIR_CONSUMER_APPLY_SIGNATURE "(" LONG SHORT_TYPE ")" VOID
#define COMMPARATOR_COMPARE_SIGNATURE "(" SHORT_TYPE SHORT_TYPE ")" SHORT_TYPE
#define PREDICATE_TEST_SIGNATURE "(" SHORT_TYPE ")" BOOLEAN
#define UNARY_OPERATOR_APPLY_AS_TYPE_SIGNATURE "(" SHORT_TYPE ")" SHORT_TYPE
#define BINARY_OPERATOR_APPLY_AS_TYPE_SIGNATURE "(" SHORT_TYPE SHORT_TYPE ")" SHORT_TYPE

#define MER_H(A, B, C, D) A##B##C##D
#define MER(A, B, C, D) MER_H(A, B, C, D)

#define JNI_SUPER_METHOD(NAME) MER(Java_me_theeninja_nativearrays_core_array_, TYPE, Array_, NAME)
#define JNI_FILTERED_METHOD(NAME) MER(Java_me_theeninja_nativearrays_core_array_filtered_Filtered, TYPE, Array_, NAME)
#define JNI_UNFILTERED_METHOD(NAME) MER(Java_me_theeninja_nativearrays_core_array_unfiltered_Unfiltered, TYPE, Array_, NAME)

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
jclass filteredArrayClass;
jclass unfilteredArrayClass;

jfieldID addressField;
jfieldID sizeField;
jfieldID excludedValueField;

jmethodID filteredArrayConstructor;
jmethodID unfilteredArrayConstructor;

jmethodID consumerApply;
jmethodID indexValuePairConsumerApply;
jmethodID comparatorCompare;
jmethodID predicateTest;
jmethodID unaryOperatorApplyAsType;
jmethodID binaryOperatorApplyAsType;

#define JNI_VERSION JNI_VERSION_10

void check(JNIEnv* env) {
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionDescribe(env);
    }
}

void initializeArrayClassGlobalReferences(JNIEnv* env) {
    jclass localArrayClass = (*env)->FindClass(env, ARRAY_CLASS_NAME);
    jclass localFilteredArrayClass = (*env)->FindClass(env, FILTERED_ARRAY_CLASS_NAME);
    jclass localUnfilteredArrayClass = (*env)->FindClass(env, UNFILTERED_ARRAY_CLASS_NAME);

    arrayClass = (*env)->NewGlobalRef(env, localArrayClass);
    filteredArrayClass = (*env)->NewGlobalRef(env, localFilteredArrayClass);
    unfilteredArrayClass = (*env)->NewGlobalRef(env, localUnfilteredArrayClass);
}

void freeArrayClassGlobalReferences(JNIEnv* env) {
    (*env)->DeleteGlobalRef(env, arrayClass);
    (*env)->DeleteGlobalRef(env, filteredArrayClass);
    (*env)->DeleteGlobalRef(env, unfilteredArrayClass);
}

void initializeArrayConstructors(JNIEnv* env) {
    A
    filteredArrayConstructor = (*env)->GetMethodID(env, filteredArrayClass, CONSTRUCTOR_METHOD, FILTERED_ARRAY_CONSTRUCTOR_SIGNATURE);
    A
    unfilteredArrayConstructor = (*env)->GetMethodID(env, unfilteredArrayClass, CONSTRUCTOR_METHOD, UNFILTERED_ARRAY_CONSTRUCTOR_SIGNATURE);
    A
}

void initializeArraySuperClassFields(JNIEnv* env) {
    jclass localArraySuperClass = (*env)->FindClass(env, SUPER_ARRAY_CLASS_NAME);

    addressField = (*env)->GetFieldID(env, localArraySuperClass, ADDRESS_FIELD, LONG);
    sizeField = (*env)->GetFieldID(env, localArraySuperClass, SIZE_FIELD, LONG);
}

void initializeArrayFilteredClassFields(JNIEnv* env) {
    excludedValueField = (*env)->GetFieldID(env, filteredArrayClass, EXCLUDED_VALUE_FIELD, SHORT_TYPE);
}

void initializeFunctionalInterfaceMethodIDs(JNIEnv* env) {
    A
    jclass localConsumerClass = (*env)->FindClass(env, CONSUMER_CLASS_NAME);
            check(env);
    A
    consumerApply = (*env)->GetMethodID(env, localConsumerClass, CONSUMER_APPLY, CONSUMER_APPLY_SIGNATURE);
        check(env);
    A
    jclass localIndexValuePairConsumerClass = (*env)->FindClass(env, INDEX_VALUE_PAIR_CONSUMER_CLASS_NAME);
          check(env);
    A
    indexValuePairConsumerApply = (*env)->GetMethodID(env, localIndexValuePairConsumerClass, CONSUMER_APPLY, INDEX_VALUE_PAIR_CONSUMER_APPLY_SIGNATURE);
        check(env);
    A
    jclass localComparatorClass = (*env)->FindClass(env, COMPARATOR_CLASS_NAME);
    check(env);
    A
    comparatorCompare = (*env)->GetMethodID(env, localComparatorClass, COMPARATOR_COMPARE, COMMPARATOR_COMPARE_SIGNATURE);
        check(env);
    A
    jclass localPredicateClass = (*env)->FindClass(env, PREDICATE_CLASS_NAME);
            check(env);
    A
    predicateTest = (*env)->GetMethodID(env, localPredicateClass, PREDICATE_TEST, PREDICATE_TEST_SIGNATURE);
        check(env);
    A
    jclass localUnaryOperatorClass = (*env)->FindClass(env, UNARY_OPERATOR_CLASS_NAME);
            check(env);
    A
    unaryOperatorApplyAsType = (*env)->GetMethodID(env, localUnaryOperatorClass, UNARY_OPERATOR_APPLY_AS_TYPE, UNARY_OPERATOR_APPLY_AS_TYPE_SIGNATURE);
        check(env);
    A
    jclass localBinaryOperatorClass = (*env)->FindClass(env, BINARY_OPERATOR_CLASS_NAME);
            check(env);
    A

    binaryOperatorApplyAsType = (*env)->GetMethodID(env, localBinaryOperatorClass, BINARY_OPERATOR_APPLY_AS_TYPE, BINARY_OPERATOR_APPLY_AS_TYPE_SIGNATURE);
    A
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    void** envAddress = (void **) &env;

    A

    if ((*vm)->GetEnv(vm, envAddress, JNI_VERSION) != JNI_OK) {
        return JNI_ERR;
    }

    A
    initializeArrayClassGlobalReferences(env);
    check(env);
    A
    initializeArrayConstructors(env);
    check(env);
    A
    initializeArraySuperClassFields(env);
    check(env);
    A
    initializeArrayFilteredClassFields(env);
        check(env);
    A
    initializeFunctionalInterfaceMethodIDs(env);
        check(env);

    A
    check(env);

    return JNI_VERSION;
}

uint_fast64_t getSize(JNIEnv* env, jobject instance) {
    return (uint_fast64_t) (*env)->GetLongField(env, instance, sizeField);
}

void setSize(JNIEnv* env, jobject instance, uint_fast64_t size) {
    (*env)->SetLongField(env, instance, sizeField, size);
}

JAVA_TYPE* getAddress(JNIEnv* env, jobject instance) {
    return (JAVA_TYPE*) (*env)->GetLongField(env, instance, addressField);
}

#define GET_FIELD_H2(A) Get##A##Field
#define GET_FIELD_H(A) GET_FIELD_H2(A)
#define GET_FIELD GET_FIELD_H(TYPE)

JAVA_TYPE getExcludedValue(JNIEnv* env, jobject instance) {
    return (*env)->GET_FIELD(env, instance, excludedValueField);
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    void** envAddress = (void **) &env;

    if ((*vm)->GetEnv(vm, envAddress, JNI_VERSION) != JNI_OK) {
        return;
    }

    freeArrayClassGlobalReferences(env);
}

JNIEXPORT JAVA_TYPE JNICALL JNI_UNFILTERED_METHOD(get)(JNIEnv* env, jobject instance, jlong index) {
    JAVA_TYPE* address = getAddress(env, instance);

    const JAVA_TYPE jValue = address[index];

    return jValue;
}

void advance(JNIEnv* env, jobject array, JAVA_TYPE* arrayAddress, jlong* requestedIndexAddress) {
    JAVA_TYPE excludedValue = getExcludedValue(env, array);

    jlong viewSize = *requestedIndexAddress;

    for (uint_fast64_t index = 0; index < viewSize; index++) {
        const JAVA_TYPE value = arrayAddress[index];

        if (value == excludedValue) {
            (*requestedIndexAddress)++;
        }
    }
}

JNIEXPORT JAVA_TYPE JNICALL JNI_FILTERED_METHOD(get)(JNIEnv* env, jobject instance, jlong requestedIndex) {
    JAVA_TYPE* address = getAddress(env, instance);

    advance(env, instance, address, &requestedIndex);

    const JAVA_TYPE jValue = address[requestedIndex];

    return jValue;
}

JNIEXPORT void JNICALL JNI_UNFILTERED_METHOD(set)(JNIEnv* env, jobject instance, jlong index, JAVA_TYPE value) {
    JAVA_TYPE* address = getAddress(env, instance);

    address[index] = value;
}

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(set)(JNIEnv* env, jobject instance, jlong requestedIndex, JAVA_TYPE value) {
    JAVA_TYPE* address = getAddress(env, instance);

    advance(env, instance, address, &requestedIndex);

    address[requestedIndex] = value;
}

JNIEXPORT void JNICALL JNI_SUPER_METHOD(close)(JNIEnv* env, jobject instance) {

}

#define ARRAY_H(JAVA_TYPE) JAVA_TYPE##Array
#define ARRAY(JAVA_TYPE) ARRAY_H(JAVA_TYPE)

#define NEW_ARRAY_H(TYPE) New##TYPE##Array
#define NEW_ARRAY(TYPE) NEW_ARRAY_H(TYPE)

#define GET_ARRAY_ELEMENTS_H(TYPE) Get##TYPE##ArrayElements
#define GET_ARRAY_ELEMENTS(TYPE) GET_ARRAY_ELEMENTS_H(TYPE)

#define RELEASE_ARRAY_ELEMENTS_H(TYPE) Release##TYPE##ArrayElements
#define RELEASE_ARRAY_ELEMENTS(TYPE) RELEASE_ARRAY_ELEMENTS_H(TYPE)

JNIEXPORT ARRAY(JAVA_TYPE) JNICALL JNI_UNFILTERED_METHOD(toJavaArray)(JNIEnv* env, jobject instance) {
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

JNIEXPORT ARRAY(JAVA_TYPE) JNICALL JNI_FILTERED_METHOD(toJavaArray)(JNIEnv* env, jobject instance) {
    jlong size = getSize(env, instance);

    if (size > 1LL << (8 * sizeof(JAVA_TYPE) - 1)) {
        return NULL;
    }

    ARRAY(JAVA_TYPE) javaArray = (*env)->NEW_ARRAY(TYPE)(env, size);
    JAVA_TYPE* javaValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    if (javaValues == NULL) {
        return NULL;
    }

    JAVA_TYPE* address = getAddress(env, instance);

    uint_fast64_t filterAwareIndex = 0;

    for (uint_fast64_t filterUnawareIndex = 0; filterUnawareIndex < size; filterUnawareIndex++) {
        const JAVA_TYPE value = address[filterUnawareIndex];

        if (value != excludedValue) {
            javaValues[filterAwareIndex++] = value;
        }
    }

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaValues, 0);

    return javaArray;
}

JNIEXPORT void JNICALL JNI_UNFILTERED_METHOD(intoJavaArray)(JNIEnv* env, jobject instance, ARRAY(JAVA_TYPE) javaArray) {
    jsize javaValuesLength = (*env)->GetArrayLength(env, javaArray);
    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);

    JAVA_TYPE* address = getAddress(env, instance);

    memcpy(javaArrayValues, address, javaValuesLength * sizeof(JAVA_TYPE));

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaArrayValues, 0);
}

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(intoJavaArray)(JNIEnv* env, jobject instance, ARRAY(JAVA_TYPE) javaArray) {
    jsize javaValuesLength = (*env)->GetArrayLength(env, javaArray);
    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);

    JAVA_TYPE* address = getAddress(env, instance);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    for (uint_fast64_t filterUnawareIndex = 0, filterAwareIndex = 0; filterUnawareIndex < javaValuesLength; filterUnawareIndex++) {
        const JAVA_TYPE value = address[filterUnawareIndex];

        if (value != excludedValue) {
            javaArrayValues[filterAwareIndex++] = value;
        }
    }

    (*env)->RELEASE_ARRAY_ELEMENTS(TYPE)(env, javaArray, javaArrayValues, 0);
}

JNIEXPORT jlong JNICALL JNI_SUPER_METHOD(malloc)(JNIEnv* env, jobject arrayInstance) {
    printf("Malloc 1\n");
    printf("Malloc 2\n");

    uint_fast64_t size = getSize(env, arrayInstance);

    printf("Size %lld\n", size);

    JAVA_TYPE* address = malloc(sizeof(JAVA_TYPE) * size);
    printf("Malloc 3\n");

    printf("Allocated address %p\n", address);

    return (jlong) address;
}

JNIEXPORT jobject JNICALL JNI_SUPER_METHOD(fromJavaArray)(JNIEnv* env, jclass arrayClass, ARRAY(JAVA_TYPE) javaArray) {
    printf("from 0\n");
    jsize size = (*env)->GetArrayLength(env, javaArray);
    printf("from 1\n");
    jobject unfilteredArray = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);

    printf("from 2\n");

    JAVA_TYPE* javaArrayValues = (*env)->GET_ARRAY_ELEMENTS(TYPE)(env, javaArray, 0);
        printf("from 3\n");

    JAVA_TYPE* address = getAddress(env, unfilteredArray);

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

    return unfilteredArray;
}

JNIEXPORT jboolean JNI_UNFILTERED_METHOD(equals)(JNIEnv* env, jobject instance, jobject other) {
    if (other == NULL) {
        return JNI_FALSE;
    }

    jclass otherClass = (*env)->GetObjectClass(env, other);

    if (arrayClass != otherClass) {
        return JNI_FALSE;
    }

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    jlong size = getSize(env, instance);
    jlong otherSize = getSize(env, other);

    if (size != otherSize) {
        return JNI_FALSE;
    }

    return memcmp(address, otherAddress, size * sizeof(JAVA_TYPE));
}

JNIEXPORT jboolean JNI_FILTERED_METHOD(equals)(JNIEnv* env, jobject instance, jobject other) {
    if (other == NULL) {
        return JNI_FALSE;
    }

    jclass otherClass = (*env)->GetObjectClass(env, other);

    if (arrayClass != otherClass) {
        return JNI_FALSE;
    }

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    jlong size = getSize(env, instance);
    jlong otherSize = getSize(env, other);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    if (size != otherSize) {
        return JNI_FALSE;
    }

    uint_fast64_t filterAwareIndex = 0;
    uint_fast64_t otherFilterAwareIndex = 0;

    while (filterAwareIndex < size && otherFilterAwareIndex < otherSize) {
        while (address[filterAwareIndex] == excludedValue) {
            filterAwareIndex++;
        }

        while (otherAddress[otherFilterAwareIndex] == excludedValue) {
            otherFilterAwareIndex++;
        }

        const JAVA_TYPE value = address[filterAwareIndex];
        const JAVA_TYPE otherValue = otherAddress[otherFilterAwareIndex];

        if (value != otherValue) {
            return JNI_FALSE;
        }
    }

    return JNI_TRUE;
}

#define HASHCODE_PRIME_NUMBER 7

JNIEXPORT jint JNICALL JNI_FILTERED_METHOD(hashCode)(JNIEnv* env, jobject array) {
    JAVA_TYPE* address = getAddress(env, array);
    uint_fast64_t size = getSize(env, array);

    JAVA_TYPE excludedValue = getExcludedValue(env, array);

    jint hashCode = 1;

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (value != excludedValue) {
            hashCode *= HASHCODE_PRIME_NUMBER;
            hashCode += value;
        }
    }

    return hashCode;
}

JNIEXPORT jint JNICALL JNI_UNFILTERED_METHOD(hashCode)(JNIEnv* env, jobject array) {
JAVA_TYPE* address = getAddress(env, array);
    uint_fast64_t size = getSize(env, array);

    jint hashCode = 1;

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        hashCode *= HASHCODE_PRIME_NUMBER;
        hashCode += value;
    }

    return hashCode;
}

#define ARRAY_START "["
#define ARRAY_END "]"
#define ARRAY_SEPARATOR ", "

#define MAX_JAVA_TYPE_LENGTH 10
#define BRACKETS_LENGTH 2
#define GET_SEPARATOR_LENGTH(arrayLength) (((arrayLength) - 1) * 2)
#define NULL_TERMINATOR_LENGTH 1

#define GET_REQUIRED_LENGTH(arrayLength) ((arrayLength) * (MAX_JAVA_TYPE_LENGTH) + (GET_SEPARATOR_LENGTH(arrayLength)) + (BRACKETS_LENGTH) + (NULL_TERMINATOR_LENGTH))

JNIEXPORT jstring JNICALL JNI_UNFILTERED_METHOD(toString)(JNIEnv* env, jobject instance) {
    uint_fast64_t size = getSize(env, instance);

    const JAVA_TYPE requiredLength = GET_REQUIRED_LENGTH(size); // maximum size of jstring is JAVA_TYPE

    printf("required length " JAVA_TYPE_FORMAT_SPECIFIER "\n", requiredLength);

    // + 1 is for the null byte
    char* string = malloc(sizeof(char) * requiredLength + 1);

    JAVA_TYPE* address = getAddress(env, instance);

    printf("current string %s\n", string);

    strcpy(string, ARRAY_START);

    printf("current string %s\n", string);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        // + 1 is for the null byte
        char valueString[MAX_JAVA_TYPE_LENGTH + 1];

        sprintf(valueString, JAVA_TYPE_FORMAT_SPECIFIER, value);

        printf("value string %s\n", valueString);

        strcat(string, valueString);

        printf("current string %s\n", string);

        if (index != size - 1) {
            strcat(string, ARRAY_SEPARATOR);
            printf("current string %s\n", string);
        }
    }

    strcat(string, ARRAY_END);

    jstring jString = (*env)->NewStringUTF(env, string);

    return jString;
}

JNIEXPORT jstring JNICALL JNI_FILTERED_METHOD(toString)(JNIEnv* env, jobject instance) {
    uint_fast64_t size = getSize(env, instance);

    const JAVA_TYPE requiredLength = GET_REQUIRED_LENGTH(size); // maximum size of jstring is JAVA_TYPE

    // + 1 is for the null byte
    char* string = malloc(sizeof(char) * requiredLength + 1);

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    strcpy(string, ARRAY_START);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (value == excludedValue) {
            continue;
        }

        // + 1 is for the null byte
        char valueString[MAX_JAVA_TYPE_LENGTH + 1];

        sprintf(valueString, JAVA_TYPE_FORMAT_SPECIFIER, value);

        strcat(string, valueString);

        if (index != size - 1) {
            strcat(string, ARRAY_SEPARATOR);
        }
    }

    strcat(string, ARRAY_END);

    jstring jString = (*env)->NewStringUTF(env, string);

    return jString;
}

#define CONSUME_METHOD_NAME "apply"

JNIEXPORT void JNICALL JNI_UNFILTERED_METHOD(forEachIndexValuePair)(JNIEnv* env, jobject instance, jobject indexValuePairConsumer) {
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

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(forEachIndexValuePair)(JNIEnv* env, jobject instance, jobject indexValuePairConsumer) {
    uint_fast64_t size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (value != excludedValue) {
            (*env)->CallVoidMethod(env, indexValuePairConsumer, indexValuePairConsumerApply, index, value);
        }
    }
}

#define CALL_TYPE_METHOD_H2(A) Call##A##Method
#define CALL_TYPE_METHOD_H(A) CALL_TYPE_METHOD_H2(A)
#define CALL_TYPE_METHOD CALL_TYPE_METHOD_H(TYPE)

JNIEXPORT jobject JNICALL JNI_UNFILTERED_METHOD(map)(JNIEnv* env, jobject oldArray, jobject mapper) {
    const uint_fast64_t size = getSize(env, oldArray);
    JAVA_TYPE* oldAddress = getAddress(env, oldArray);

    jobject newUnfilteredArray = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);
    JAVA_TYPE* newAddress = getAddress(env, newUnfilteredArray);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = oldAddress[index];
        const JAVA_TYPE newValue = (*env)->CALL_TYPE_METHOD(env, mapper, unaryOperatorApplyAsType, oldValue);

        newAddress[index] = newValue;
    }

    return newUnfilteredArray;
}

JNIEXPORT jobject JNICALL JNI_FILTERED_METHOD(rMap)(JNIEnv* env, jobject oldArray, jobject mapper) {
    const uint_fast64_t size = getSize(env, oldArray);
    JAVA_TYPE* oldAddress = getAddress(env, oldArray);

    JAVA_TYPE excludedValue = getExcludedValue(env, oldArray);

    jobject newArray = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);
    JAVA_TYPE* newAddress = getAddress(env, newArray);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = oldAddress[index];

        if (oldValue != excludedValue) {
            const JAVA_TYPE newValue = (*env)->CALL_TYPE_METHOD(env, mapper, unaryOperatorApplyAsType, oldValue);

            newAddress[index] = newValue;
        }
    }

    return newArray;
}

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(iMap)(JNIEnv* env, jobject array, jobject mapper) {
    uint_fast64_t size = getSize(env, array);
    JAVA_TYPE* address = getAddress(env, array);

    JAVA_TYPE excludedValue = getExcludedValue(env, array);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = address[index];

        if (oldValue != excludedValue) {
             const JAVA_TYPE newValue = (*env)->CALL_TYPE_METHOD(env, mapper, unaryOperatorApplyAsType, oldValue);

             address[index] = newValue;
        }
    }
}

JNIEXPORT jobject JNICALL JNI_FILTERED_METHOD(iFilter)(JNIEnv* env, jobject filteredArray, jobject filterer) {
    uint_fast64_t size = getSize(env, filteredArray);
    JAVA_TYPE* address = getAddress(env, filteredArray);

    JAVA_TYPE excludedValue = getExcludedValue(env, filteredArray);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = address[index];

        if (oldValue != excludedValue && (*env)->CallBooleanMethod(env, filterer, predicateTest, oldValue) == JNI_FALSE) {
            address[index] = excludedValue;
        }
    }

    return filteredArray;
}


JNIEXPORT jobject JNICALL JNI_UNFILTERED_METHOD(iFilter)(JNIEnv* env, jobject unfilteredArray, jobject filterer) {
    uint_fast64_t size = getSize(env, unfilteredArray);
    JAVA_TYPE* address = getAddress(env, unfilteredArray);

    jobject filteredArray = (*env)->NewObject(env, filteredArrayClass, filteredArrayConstructor, address, size);

    JAVA_TYPE excludedValue = getExcludedValue(env, unfilteredArray);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE oldValue = address[index];

        if ((*env)->CallBooleanMethod(env, filterer, predicateTest, oldValue) == JNI_FALSE) {
            address[index] = excludedValue;
        }
    }

    return filteredArray;
}

JNIEXPORT jobject JNICALL JNI_FILTERED_METHOD(rFilter)(JNIEnv* env, jobject filteredArray, jobject filterer) {
    uint_fast64_t size = getSize(env, filteredArray);
    JAVA_TYPE* address = getAddress(env, filteredArray);

    jobject unfilteredArray = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);
    JAVA_TYPE* otherAddress = getAddress(env, unfilteredArray);

    JAVA_TYPE excludedValue = getExcludedValue(env, filteredArray);

    uint_fast64_t unfilteredIndex = 0;

    for (uint_fast64_t filteredIndex = 0; filteredIndex < size; filteredIndex++) {
        const JAVA_TYPE oldValue = address[filteredIndex];

        if (oldValue != excludedValue && (*env)->CallBooleanMethod(env, filterer, predicateTest, oldValue) == JNI_TRUE) {
            otherAddress[unfilteredIndex++] = oldValue;
        }
    }

    setSize(env, unfilteredArray, unfilteredIndex);

    return unfilteredArray;
}

JNIEXPORT jobject JNICALL JNI_UNFILTERED_METHOD(rFilter)(JNIEnv* env, jobject oldUnfilteredArray, jobject filterer) {
    uint_fast64_t size = getSize(env, oldUnfilteredArray);
    JAVA_TYPE* address = getAddress(env, oldUnfilteredArray);

    jobject newUnfilteredArray = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);
    JAVA_TYPE* otherAddress = getAddress(env, newUnfilteredArray);

    JAVA_TYPE excludedValue = getExcludedValue(env, oldUnfilteredArray);

    uint_fast64_t unfilteredIndex = 0;

    for (uint_fast64_t filteredIndex = 0; filteredIndex < size; filteredIndex++) {
        const JAVA_TYPE oldValue = address[filteredIndex];

        if (oldValue != excludedValue && (*env)->CallBooleanMethod(env, filterer, predicateTest, oldValue) == JNI_TRUE) {
            otherAddress[unfilteredIndex++] = oldValue;
        }
    }

    setSize(env, newUnfilteredArray, unfilteredIndex);

    return newUnfilteredArray;
}

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(forEachValue)(JNIEnv* env, jobject instance, jobject consumer) {
    uint_fast64_t size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        if (value != excludedValue) {
            (*env)->CallVoidMethod(env, consumer, indexValuePairConsumerApply, value);
        }
    }
}

JNIEXPORT void JNICALL JNI_UNFILTERED_METHOD(forEachValue)(JNIEnv* env, jobject instance, jobject consumer) {
    uint_fast64_t size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    for (uint_fast64_t index = 0; index < size; index++) {
        const JAVA_TYPE value = address[index];

        (*env)->CallVoidMethod(env, consumer, indexValuePairConsumerApply, value);
    }
}

JNIEXPORT jobject JNICALL JNI_UNFILTERED_METHOD(copy)(JNIEnv* env, jobject instance) {
    uint_fast64_t size = getSize(env, instance);

    jobject other = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    memcpy(otherAddress, address, sizeof(JAVA_TYPE) * size);

    return other;
}

JNIEXPORT jobject JNICALL JNI_FILTERED_METHOD(copy)(JNIEnv* env, jobject instance) {
    uint_fast64_t size = getSize(env, instance);

    jobject other = (*env)->NewObject(env, unfilteredArrayClass, unfilteredArrayConstructor, size);

    JAVA_TYPE* address = getAddress(env, instance);
    JAVA_TYPE* otherAddress = getAddress(env, other);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    for (uint_fast64_t filteredIndex = 0, unfilteredIndex = 0; filteredIndex < size; filteredIndex++) {
        const JAVA_TYPE value = address[filteredIndex];

        if (value != excludedValue) {
            otherAddress[unfilteredIndex++] = value;
        }
    }

    return other;
}

JNIEXPORT void JNICALL JNI_UNFILTERED_METHOD(fill)(JNIEnv* env, jobject instance, JAVA_TYPE value) {
    jlong size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    memset(address, value, sizeof(JAVA_TYPE) * size);
}

JNIEXPORT void JNICALL JNI_FILTERED_METHOD(fill)(JNIEnv* env, jobject instance, JAVA_TYPE fillingValue) {
    jlong size = getSize(env, instance);
    JAVA_TYPE* address = getAddress(env, instance);

    JAVA_TYPE excludedValue = getExcludedValue(env, instance);

    for (uint_fast64_t filteredIndex = 0; filteredIndex < size; filteredIndex++) {
        const JAVA_TYPE oldValue = address[filteredIndex];

        if (oldValue != excludedValue) {
            address[filteredIndex] = fillingValue;
        }
    }
}

JNIEXPORT jlong JNICALL JNI_SUPER_METHOD(searchForwards)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {
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

JNIEXPORT jlong JNICALL JNI_SUPER_METHOD(searchBackwards)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {

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

JNIEXPORT jlong JNICALL JNI_SUPER_METHOD(count)(JNIEnv* env, jobject instance, JAVA_TYPE requestedValue) {
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