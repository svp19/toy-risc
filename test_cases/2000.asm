    .data
n:
	10
	.text
main:
    addi %x0, 0, %x3
    addi %x0, 2000, %x30
    addi %x0, 2000, %x30
    addi %x3, 1, %x3
	blt %x3, %x30, main
loop:
    end
