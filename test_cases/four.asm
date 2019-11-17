.data
n:
	10
	.text
main:
	addi %x0, 5, %x5
loop:
	addi %x0, 0, %x3
	addi %x0, 1, %x3
	subi %x5, 1, %x5
	blt %x0, %x5, loop
    end