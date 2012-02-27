fileReader = open("text2trim.txt", 'r')
fileWriter = open("trimmed.txt", 'w')
for line in fileReader:
	if len(line) == 1:
		fileWriter.write(line)
	else:
		fileWriter.write(line[:-1] + ' ')
