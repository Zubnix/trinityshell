%define NIO_BUFFER_TYPEMAP(CTYPE, BUFFERTYPE)
%typemap(jni) CTYPE* "jobject"
%typemap(jtype) CTYPE* "BUFFERTYPE"
%typemap(jstype) CTYPE* "BUFFERTYPE"
%typemap(javain, pre="    assert $javainput.isDirect() : \"Buffer must be allocated direct.\";") CTYPE* "$javainput"
%typemap(javaout) CTYPE* {	
    return $jnicall;
}
%typemap(in) CTYPE* {
  $1 = (*jenv)->GetDirectBufferAddress(jenv, $input);
  if ($1 == NULL) {
    SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, "Unable to get address of direct buffer. Buffer must be allocated direct.");
  }
}
%typemap(memberin) CTYPE* {
  if ($input) {
    $1 = $input;
  } else {
    $1 = 0;
  }
}
%typemap(out) CTYPE* {
	//TODO every method that returns a pointer to memory as a capacity defined somewhere (or at least it should have).
	//TODO provide a mapping for these methods and create a direct buffer with the correct capacity information.
	//For now we provide a value that we've just sucked out of our thumb. (max signed int value).
	$result = (*jenv)->NewDirectByteBuffer(jenv,result, 2147483647);
}
%typemap(freearg) CTYPE* ""
%enddef

//bytebuffer all the things!!!
NIO_BUFFER_TYPEMAP(void, java.nio.ByteBuffer);
//TODO char pointers are almost exclusively used for strings in xcb, so no need to wrap it into a bytebyffer.
//NIO_BUFFER_TYPEMAP(char, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(unsigned char, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(short, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(unsigned short, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(int, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(unsigned int, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(long, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(unsigned long, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(float, java.nio.ByteBuffer);
NIO_BUFFER_TYPEMAP(double, java.nio.ByteBuffer);
#undef NIO_BUFFER_TYPEMAP