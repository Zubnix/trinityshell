//Mapping for xcb_send_event:

%define CLIENT_MESSAGE_TYPEMAP(TYPE)

%typemap(jni) char* EVENT "jlong"
%typemap(jtype) char* EVENT "TYPE"
%typemap(jstype) char* EVENT "TYPE"

%typemap(javain) char* EVENT "$javainput"
%typemap(javaout) char* EVENT {	
    return $jnicall;
}

%typemap(in) char* EVENT {
  $1 = (char*) $input;
}

%typemap(memberin) char* EVENT {
  if ($input) {
    $1 = $input;
  } else {
    $1 = 0;
  }
}

%typemap(freearg) char* EVENT ""

%enddef

CLIENT_MESSAGE_TYPEMAP(xcbjb.xcb_generic_event_t);

%apply char* EVENT {  char *event };