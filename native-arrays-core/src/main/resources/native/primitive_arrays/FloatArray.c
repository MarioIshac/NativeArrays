// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#define TYPE_INCLUSION

#define JAVA_TYPE jfloat
#define TYPE Float
#define SHORT_TYPE "F"
#define JAVA_TYPE_FORMAT_SPECIFIER "%f"

#include "../array.c"

#undef TYPE_INCLUSION