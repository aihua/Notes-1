# Get current dir of makefile
local_dir := $(dir $(word $(words $(MAKEFILE_LIST)), $(MAKEFILE_LIST)))
local_lib := $(out_dir)/libdb.a

include lib.mk

$(local_lib) : $(local_obj)
	$(AR) $(ARFLAGS) $@ $^
