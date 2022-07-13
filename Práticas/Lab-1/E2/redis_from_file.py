
redis = dict()

with open("names.txt", "r") as file:
    for line in file:
        if line[0] not in redis:
            redis[line[0]] = 1
        else :
            redis[line[0]] += 1

with open("names_counting.txt", "w") as file:
    for letter, occurrences in redis.items():
        file.write("SET {} {}\n".format(letter.upper(), occurrences))