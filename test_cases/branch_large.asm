    .data
n:
	10
	.text
main:
    addi %x0, 0, %x3
    addi %x0, 2000, %x30
    addi %x0, 2000, %x30
    addi %x3, 1, %x3
    addi %x0, 2000, %x29
	blt %x3, %x30, loop
    end
loop:
    addi %x0, 2000, %x29
    addi %x0, 2000, %x29
    addi %x0, 2000, %x29
    