// Do not generate the default proxy constructor or destructor
%nodefaultctor xcb_generic_error_t;
%nodefaultdtor xcb_generic_error_t;

// Add in pure Java code proxy constructor
%typemap(javacode) xcb_generic_error_t %{
  /** This constructor creates the proxy which initially does not create nor own any C memory */
  public xcb_generic_error_t() {
    this(0, false);
  }
%}

// Type typemaps for marshalling xcb_generic_error_t **
%typemap(jni) xcb_generic_error_t ** "jobject"
%typemap(jtype) xcb_generic_error_t ** "xcb_generic_error_t"
%typemap(jstype) xcb_generic_error_t ** "xcb_generic_error_t"

// Typemaps for xcb_generic_error_t ** as a parameter output type
%typemap(in) xcb_generic_error_t ** (xcb_generic_error_t *ppxcb_generic_error_t = 0) %{
  $1 = &ppxcb_generic_error_t;
%}
%typemap(argout) xcb_generic_error_t ** {
  // Give Java proxy the C pointer (of newly created object)
  jclass clazz = (*jenv)->FindClass(jenv, "xcbjb/xcb_generic_error_t");
  jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
  jlong cPtr = 0;
  *(xcb_generic_error_t **)&cPtr = *$1;
  (*jenv)->SetLongField(jenv, $input, fid, cPtr);
}
%typemap(javain) xcb_generic_error_t ** "$javainput"