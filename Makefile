##
# Hanabi
#
# @file
# @version 0.1

hanabi:
	javac Driver.java -d build

Phony: run

run: hanabi
	cd build && \
	java Driver

clean:
	rm -rf build

# end
