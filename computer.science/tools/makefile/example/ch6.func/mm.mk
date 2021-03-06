#sample Makefile
objects := main.o kbd.o command.o display.o \
		insert.o search.o files.o utils.o

#sources := $(objects:.o=.c)
#sources := $(patsubst %.o, %.c, $(objects))
#$(info $(sources))

linux := debain
$(info $(subst debain,ubuntu,$(linux)))

$(info $(objects:%.o=%.c))

#$(info $(patsubst %.o,%.c,$(objects)))

STR =        a    b 		c    
$(info $(strip $(STR)))

$(info $(findstring test,this is a test))

#$(info $(filter %.c, a.c b.c d.o test.c g.cc)) 
$(info $(filter-out %.c %.h, a.c b.c d.o test.c g.cc)) 

$(info $(sort z f d a j k l j z k l))

edit : $(objects)
	touch edit
main.o : main.c defs.h
	touch main.o
kbd.o : kbd.c defs.h command.h
	touch kbd.o
command.o : command.c defs.h command.h
	touch command.o
display.o : display.c defs.h buffer.h
	touch display.o
insert.o : insert.c defs.h buffer.h
	touch insert.o
search.o : search.c defs.h buffer.h
	touch search.o
files.o : files.c defs.h buffer.h command.h
	touch files.o
utils.o : utils.c defs.h
	touch utils.o
clean :
	@rm -Rf edit $(objects)

gen:
	touch main.c defs.h command.h buffer.h kbd.c command.c display.c \
	insert.c search.c files.c utils.c 
