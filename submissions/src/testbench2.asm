.data
n:
	10
	.text
main:
    addi %x0, 10, %x3
loop:
	load %x0, 0, %x10
	load %x0, 1, %x10
	load %x0, 2, %x10
	load %x0, 3, %x10
	load %x0, 4, %x10
	load %x0, 5, %x10
	load %x0, 6, %x10
	load %x0, 7, %x10
	load %x0, 8, %x10
	load %x0, 9, %x10
	load %x0, 10, %x10
	load %x0, 11, %x10
	load %x0, 12, %x10
	load %x0, 13, %x10
	load %x0, 14, %x10
	load %x0, 15, %x10
	load %x0, 16, %x10
    subi %x3, 1, %x3
    blt %x0, %x3, loop
    end