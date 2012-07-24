typedef signed char int8_t;
typedef signed short int16_t;
typedef signed int int32_t;
typedef unsigned char uint8_t;
typedef unsigned short uint16_t;
//for performance reasons we typedef uint32_t to c int, this is because a java long (which will map to a c unsigned int) requires 2 operations in the jvm.
typedef signed int uint32_t;