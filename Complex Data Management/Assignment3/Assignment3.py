# Vasilis Nikas
# AM: 3143

rndFilePath = "rnd.txt"
seq1FilePath = "seq1.txt"
seq2FilePath = "seq2.txt"

import sys
class MinHeap:

    def __init__(self, maxsize):
        self.maxsize = maxsize
        self.size = 0
        self.Heap = [0] * (self.maxsize + 1)
        self.Heap[0] = -1 * sys.maxsize
        self.FRONT = 1

    # Function to return the position of
    # parent for the node currently
    # at pos
    def parent(self, pos):
        return pos // 2

    # Function to return the position of
    # the left child for the node currently
    # at pos
    def leftChild(self, pos):
        return 2 * pos

    # Function to return the position of
    # the right child for the node currently
    # at pos
    def rightChild(self, pos):
        return (2 * pos) + 1

    # Function that returns true if the passed
    # node is a leaf node
    def isLeaf(self, pos):
        return pos * 2 > self.size

    # Function to swap two nodes of the heap
    def swap(self, fpos, spos):
        self.Heap[fpos], self.Heap[spos] = self.Heap[spos], self.Heap[fpos]

    # Function to heapify the node at pos
    def minHeapify(self, pos):

        # If the node is a non-leaf node and greater
        # than any of its child
        if not self.isLeaf(pos):
            if (self.Heap[pos] > self.Heap[self.leftChild(pos)] or
                    self.Heap[pos] > self.Heap[self.rightChild(pos)]):

                # Swap with the left child and heapify
                # the left child
                if self.Heap[self.leftChild(pos)] < self.Heap[self.rightChild(pos)]:
                    self.swap(pos, self.leftChild(pos))
                    self.minHeapify(self.leftChild(pos))

                # Swap with the right child and heapify
                # the right child
                else:
                    self.swap(pos, self.rightChild(pos))
                    self.minHeapify(self.rightChild(pos))

    # Function to insert a node into the heap
    def insert(self, element):
        if self.size >= self.maxsize:
            return
        self.size += 1
        self.Heap[self.size] = element

        current = self.size

        while self.Heap[current] < self.Heap[self.parent(current)]:
            self.swap(current, self.parent(current))
            current = self.parent(current)

    # Function to build the min heap using
    # the minHeapify function
    def minHeap(self):

        for pos in range(self.size // 2, 0, -1):
            self.minHeapify(pos)

    # Function to remove and return the minimum
    # element from the heap
    def remove(self):

        popped = self.Heap[self.FRONT]
        self.Heap[self.FRONT] = self.Heap[self.size]
        self.size -= 1
        self.minHeapify(self.FRONT)
        return popped
    # Function to return the minimum element from the heap
    def getTop(self):
        return self.Heap[self.FRONT]

#print("Top k should be: ")
#k = int(input())
k = int(sys.argv[1])
rndFile = open(rndFilePath, "r")
seq1File = open(seq1FilePath, "r")
seq2File = open(seq2FilePath, "r")
tempDict = {}

# Reading the lines from the rndFile and saving the values in a list R
for line in rndFile:
    values = line.split(" ")
    tempDict[int(values[0])] = float(values[1])

R = [tempDict.get(i, 'default') for i in range(max(tempDict)+1)]

lowerLimitOfScore = {}
fullScore = {}
objectsCounter = 0
currentseq1Value = currentseq2Value = 0
thresholdT = 0
objectXseq = {}
minHeap = MinHeap(k)
linesReaded = 0
results = []
existInHeap = {}
checkedList = []
readedFrom1File = {}

# Reading the lines one by one from each file
for line1, line2 in zip(seq1File, seq2File):
    linesReaded += 1
    # Getting an object from the second file
    valuesSeq1 = line1.split(" ")
    tempId = int(valuesSeq1[0])
    # If the key is none it means we never seen this ID before
    if lowerLimitOfScore.get(tempId) is None:
        lowerLimitOfScore[tempId] = float(valuesSeq1[1]) + R[tempId]
        readedFrom1File[tempId] = float(valuesSeq1[1]) + R[tempId]
        objectXseq[tempId] = "seq1"
        currentseq1Value = float(valuesSeq1[1])
        thresholdT = currentseq1Value + currentseq2Value + 5
        if objectsCounter > k:
            if lowerLimitOfScore[tempId] > minHeap.getTop():
                minHeap.remove()
                minHeap.insert(lowerLimitOfScore[tempId])
        objectsCounter += 1
    # Else it means we have seen it in the other file so we just sum the 3 values
    else:
        fullScore[tempId] = lowerLimitOfScore[tempId] + float(valuesSeq1[1])
        readedFrom1File.pop(tempId)
        objectXseq[tempId] = "seq1"
        currentseq1Value = float(valuesSeq1[1])
        thresholdT = currentseq1Value + currentseq2Value + 5
        if objectsCounter > k:
            if fullScore[tempId] > minHeap.getTop():
                minHeap.remove()
                minHeap.insert(fullScore[tempId])
                existInHeap[tempId] = True
        objectsCounter += 1
    # Creating the heap for the first k elements
    if (objectsCounter == k):
        for i in lowerLimitOfScore:
            minHeap.insert(lowerLimitOfScore[i])
        for i in fullScore:
            minHeap.insert(fullScore[i])
    # First reason to terminate the program
    if (thresholdT <= minHeap.getTop()):
        flag = 0
        # Checking if there is an object that his upperbound is bigger than minimum element of the heap
        for i in readedFrom1File:
            if (objectXseq[i] == "seq1"):
                upperboundX = lowerLimitOfScore[i] + currentseq2Value
            else:
                upperboundX = lowerLimitOfScore[i] + currentseq1Value
            if (upperboundX > minHeap.getTop()):
                tempList = []
                flag = 0
                break
            else:
                flag = 1
        # If flag == 1 means that there is no object with the above condition
        # and we can terminate the program printing the results in the heap
        if flag == 1:
            print("Number of sequential accesses= " + str(linesReaded))
            print("Top " + str(k) + " objects")
            tempR = []
            keyList = list(fullScore.keys())
            valuesList = list(fullScore.values())
            for i in range(k):
                temp = minHeap.remove()
                possition = valuesList.index(temp)
                tempR.append(keyList[possition])
                valuesList.remove(temp)
                keyList.pop(possition)
            for i in reversed(tempR):
                print(str(i) + ": " + str(round(fullScore[i], 2)))
            quit(5)
    # Getting an object from the second file
    linesReaded += 1
    valuesSeq2 = line2.split(" ")
    tempId = int(valuesSeq2[0])
    # If the key is none it means we never seen this ID before
    if lowerLimitOfScore.get(tempId) is None:
        lowerLimitOfScore[tempId] = float(valuesSeq2[1]) + R[tempId]
        readedFrom1File[tempId] = float(valuesSeq2[1]) + R[tempId]
        objectXseq[tempId] = "seq2"
        currentseq2Value = float(valuesSeq2[1])
        thresholdT = currentseq1Value + currentseq2Value + 5
        if objectsCounter > k:
            if lowerLimitOfScore[tempId] > minHeap.getTop():
                minHeap.remove()
                minHeap.insert(lowerLimitOfScore[tempId])
        objectsCounter += 1
    # Else it means we have seen it in the other file so we just sum the 3 values
    else:
        fullScore[tempId] = lowerLimitOfScore[tempId] + float(valuesSeq2[1])
        readedFrom1File.pop(tempId)
        objectXseq[tempId] = "seq2"
        currentseq2Value = float(valuesSeq2[1])
        thresholdT = currentseq1Value + currentseq2Value + 5
        if objectsCounter > k:
            if fullScore[tempId] > minHeap.getTop():
                minHeap.remove()
                minHeap.insert(fullScore[tempId])
                existInHeap[tempId] = True
        objectsCounter += 1
    # Creating the heap for the first k elements
    if (objectsCounter == k):
        for i in lowerLimitOfScore:
            minHeap.insert(lowerLimitOfScore[i])
        for i in fullScore:
            minHeap.insert(fullScore[i])
    # First reason to terminate the program
    if (thresholdT <= minHeap.getTop()):
        flag = 0
        # Checking if there is an object that his upperbound is bigger than minimum element of the heap
        for i in readedFrom1File:
            if (objectXseq[i] == "seq1"):
                upperboundX = lowerLimitOfScore[i] + currentseq2Value
            else:
                upperboundX = lowerLimitOfScore[i] + currentseq1Value
            if (upperboundX > minHeap.getTop()):
                tempList = []
                flag = 0
                break
            else:
                flag = 1
        # If flag == 1 means that there is no object with the above condition
        # and we can terminate the program printing the results in the heap
        if flag == 1:
            print("Number of sequential accesses= " + str(linesReaded))
            print("Top " + str(k) + " objects")
            tempR = []
            keyList = list(fullScore.keys())
            valuesList = list(fullScore.values())
            for i in range(k):
                temp = minHeap.remove()
                possition = valuesList.index(temp)
                tempR.append(keyList[possition])
                valuesList.remove(temp)
                keyList.pop(possition)
            for i in reversed(tempR):
                print(str(i) + ": " + str(round(fullScore[i], 2)))
            quit(5)
