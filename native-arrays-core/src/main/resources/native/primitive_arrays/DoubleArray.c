// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#define TYPE_INCLUSION

#define JAVA_TYPE jdouble
#define TYPE Double
#define SHORT_TYPE "D"
#define JAVA_TYPE_FORMAT_SPECIFIER "%lf"

#include "../array.c"

#undef TYPE_INCLUSION