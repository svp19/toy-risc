import pandas as pd 
import matplotlib.pyplot as plt

df = pd.read_csv('stats_l1d_1kb.csv', header=None)
desc = df.iloc[0, :]
evenodd = df.iloc[1, :]
fib = df.iloc[2, :]
pal = df.iloc[3, :]
prime = df.iloc[4, :]

x = ['8', '32', '128', '1024']

Y = [desc, evenodd, fib, pal, prime]
for y in Y:
    plt.plot(x, y)
    plt.scatter(x, y)

plt.title('L1i Cache Size VS IPC')
plt.xlabel('Cache Size (Bytes)')
plt.ylabel('IPC')
plt.legend(['descending', 'evenodd', 'fibonacci', 'palindrome', 'prime'])
plt.savefig('L1i_graph.png')
plt.show()
