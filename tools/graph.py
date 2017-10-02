#!/usr/bin/env python
"""
This tool builds a graph "Percentage of strategy wins"
run: python graph.py [game log file]

Before using please install libraries:
sudo pip instal matplotlib
sudo pip install csv
"""

import sys
import csv
import matplotlib.pyplot as plt

def main():
	input_file = sys.argv[1]
	x = []
	y = []
	with open(input_file) as inp:
		reader = csv.reader(inp, delimiter=',')
		counter = 0
		win_counter = 0
		for row in reader:
			counter = counter + 1
			if row[2] == 'Win':
				win_counter = win_counter + 1
			x.append(counter)
			y.append(100.0 * win_counter / counter)

	fig, ax0 = plt.subplots(nrows=1)
	ax0.grid(True)
	ax0.plot(x, y, '-', linewidth=2)

	plt.xlabel("Game episodes")
	plt.ylabel("Win percent")
	plt.show()

if __name__=='__main__':
	main()
