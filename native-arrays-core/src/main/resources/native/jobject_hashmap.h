#ifndef JOBJECT_HASHMAP
#define JOBJECT_HASHMAP

#include <jni.h>
#include <stdlib.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

#define PARITION_COUNT 1024

struct hashmap_node;
struct hashmap;

struct hashmap* hashmap_new();
void hashmap_put(struct hashmap*, jobject*, JAVA_TYPE*, jlong);
struct hashmap_node* hashmap_get(struct hashmap*, jobject*);
void hashmap_remove(JNIEnv*, struct hashmap*, jobject*);
void hashmap_free(JNIEnv*, struct hashmap*);

#ifdef __cplusplus
}
#endif

#endif