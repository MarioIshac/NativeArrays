// Read this for knowledge on what refs are local and global
// http://www.latkin.org/blog/2016/02/01/jni-object-lifetimes-quick-reference/

#define TYPE_INCLUSION

#define JAVA_TYPE jshort
#define TYPE Short
#define SHORT_TYPE
#define JAVA_TYPE_FORMAT_SPECIFIER "%i"

#include "../array.c"

#undef TYPE_INCLUSION