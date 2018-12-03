// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#define TYPE_INCLUSION

#define JAVA_TYPE jlong
#define TYPE Long
#define SHORT_TYPE "J"
#define JAVA_TYPE_FORMAT_SPECIFIER "%ld"

#include "../array.c"

#undef TYPE_INCLUSION