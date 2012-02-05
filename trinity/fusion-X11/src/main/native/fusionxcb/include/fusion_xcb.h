/*
 * fusion_xcb.h
 *
 *  Created on: Jul 16, 2010
 *      Author: Erik De Rijcke
 */

#ifndef FUSION_XCB_H_
#define FUSION_XCB_H_

#include <stdlib.h>
#include <string.h>
#include <stdio.h>

//xcb doesn't provide keysyms :(


#include <xcb/xcb.h>
#include <xcb/xproto.h>



#define writeError(error)\
		memmove(resultBuffer, error, sizeof(xcb_generic_error_t));\
		free( error);\

#endif /* FUSION_XCB_H_ */
