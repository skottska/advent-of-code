def fromSnafu(c):
	if c == '2': 
	  return 2
	elif c == '1':
		 return 1
	elif c == '0':
		 return 0
	elif c == '-':
		 return -1
	else:
		 return -2

def maxSnafu(power):
	if power < 1:
		return 0
	if power == 1:
		return 2
	return 2 * power + maxSnafu(power / 5)
	
def maxPower(i):
	cur = 1
	total = 0
	while (total < i):
		cur *= 5
		total += 2 * cur
	return cur

def toSnafu(i, power):
	if power < 1:
		return ""
	maxi = maxSnafu(power / 5)
	if i > 0 and i > power + maxi:
		return '2' + toSnafu(i - 2 * power, power / 5)
	if i > 0 and i > maxi:
		return '1' + toSnafu(i - power, power / 5)	
	if -i > power + maxi:
		return '=' + toSnafu(i + 2 * power, power / 5)
	if -i > maxi:
		return '-' + toSnafu(i + power, power / 5)	
	return '0' + toSnafu(i, power / 5)
		
f = open("/storage/emulated/0/Download/input.txt").read()
total = 0

for s in f.splitlines():
  power = 1
  cs = [x for x in s]
  result = 0
  for c in reversed(cs):
  	val = fromSnafu(c)
  	result += val * power
  	power *= 5
  total += result
  power = 1
  result = 0
part1 = toSnafu(total, maxPower(total))
print(f"part1={part1}")
