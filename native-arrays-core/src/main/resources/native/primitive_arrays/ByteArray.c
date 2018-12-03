// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#define TYPE_INCLUSION

#ifdef
#undef JAVA_TYPE
#define JAVA_TYPE jbyte

#define TYPE Byte
#define SHORT_TYPE "B"
#define JAVA_TYPE_FORMAT_SPECIFIER "%c"

#include "../array.c"

#undef TYPE_INCLUSION