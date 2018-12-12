#include "jobject_hashmap.h"

#define PARITION_COUNT 1024

uintptr_t hashmap_hash(jobject* jObject) {
    return (uintptr_t) jObject;
}

struct hashmap* hashmap_new() {
    struct hashmap* hashMap = malloc(sizeof(struct hashmap));

    return hashMap;
}

void hashmap_put(struct hashmap* hashMap, jobject* jObject, JAVA_TYPE* address, jlong size) {
    uintptr_t hashResult = hashmap_hash(jObject);
    uintptr_t partition = hashResult % PARITION_COUNT;

    struct hashmap_node* node = hashMap->nodes[partition];

    while (node->next != NULL) {
        node = node->next;
    }

    struct hashmap_node* jObjectNode = malloc(sizeof(struct hashmap_node));

    jObjectNode->key = jObject;
    jObjectNode->address = address;
    jObjectNode->size = size;
    node->next = jObjectNode;
    jObjectNode->previous = node;
}

struct hashmap_node* hashmap_get(struct hashmap* hashMap, jobject* jObject) {
    uintptr_t hashResult = hashmap_hash(jObject);
    uintptr_t partition = hashResult % PARITION_COUNT;

    struct hashmap_node* node = hashMap->nodes[partition];

    while (node->key != jObject || node->next == NULL) {
        node = node->next;
    }

    if (node->key != jObject) {
        return NULL;
    }

    return node;
}

void hashmap_remove(JNIEnv* env, struct hashmap* hashMap, jobject* jObject) {
    uintptr_t hashResult = hashmap_hash(jObject);
    uintptr_t partition = hashResult % PARITION_COUNT;

    struct hashmap_node* node = hashMap->nodes[partition];

    if (node->key == jObject) {
        hashMap->nodes[partition] = node->next;
        free(node->address);
        free(node);
    }
    else {
        while (node->next->key != jObject) {
            node = node->next;
        }

        struct hashmap_node* removed = node->next;

        node->next = removed->next;
        removed->next->previous = node;

        free(removed->address);
        free(removed);
    }

    (*env)->DeleteGlobalRef(env, *jObject);
}

void hashmap_free(JNIEnv* env, struct hashmap* hashMap) {
    for (int partitionIndex = 0; partitionIndex < PARITION_COUNT; partitionIndex++) {
        struct hashmap_node* node = hashMap->nodes[partitionIndex];

        while (node->next != NULL) {
            node = node->next;
        }

        while (node->previous != NULL) {
            node = node->previous;

            (*env)->DeleteGlobalRef(env, *node->next->key);

            free(node->next);
        }

        (*env)->DeleteGlobalRef(env, *node->key);

        free(node);
    }

    free(hashMap);
}