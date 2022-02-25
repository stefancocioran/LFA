JFLAGS = -g
JC = javac
JVM = java -Xss1G

all: build

sources = $(wildcard *.java)
classes = $(sources:.java=.class)

build: $(classes)

run: build
	$(JVM) Main $(problem)

labyrinth: build
	$(JVM) Labyrinth

.PHONY: clean
clean :
	rm -f *.class *~

%.class : %.java
	$(JC) $(JFLAGS) $<
