# Vasilis Nikas
# AM: 3143

filePath = 'acs2015_census_tract_data.csv'
binSize = 100

def partition(array, low, high):
    # choose the rightmost element as pivot
    pivot = array[high]
    # pointer for greater element
    i = low - 1

    # compare each element with pivot
    for j in range(low, high):
        if array[j] <= pivot:
            # If element smaller than pivot is found
            # swap it with the greater element pointed by i
            i = i + 1

            # Swapping element at i with element at j
            (array[i], array[j]) = (array[j], array[i])

    # Swap the pivot element with the greater element specified by i
    (array[i + 1], array[high]) = (array[high], array[i + 1])

    # Return the position from where partition is done
    return i + 1

def quickSort(array, low, high):
    if low < high:
        partitiontoFind = partition(array, low, high)

        # Recursive call on the left of partitiontoFind
        quickSort(array, low, partitiontoFind - 1)

        # Recursive call on the right of partitiontoFind
        quickSort(array, partitiontoFind + 1, high)

def equiWidth(ranges, min, max, results):
    binsRange = (max - min)/binSize
    currentRange = min
    numtuples = []

    while currentRange <= max:
        ranges.append(currentRange)
        currentRange += binsRange
        currentRange = round(currentRange,2) ###

    counter = 0
    i = 1
    #for i in range(len(ranges) - 1):
    for j in results:
        if i < len(ranges) and j < ranges[i]:
            counter += 1
        else:
            if i <= len(ranges):
                numtuples.append(counter)
            i += 1
            counter = 1
            #j -= 1

    return numtuples

def equiDepth(ranges, min, results):
    valuesPerBin = len(results)/binSize
    #begin from -1 because the i in for loop starts from the first element not from zero
    valuesCounter = -1
    ranges.append(min)
    numtuples = []
    for i in results:
        valuesCounter += 1
        if valuesCounter == int(valuesPerBin):
            ranges.append(i)
            numtuples.append(valuesCounter)
            valuesCounter = 0
        if (len(numtuples) > binSize - 1):
            break

    return numtuples

def findLowestLimit(array, lengthOfTheArray, target):
    # Check if the target is outside our array
    if (target < array[0]):
        return array[0]
    if (target > array[lengthOfTheArray - 1]):
        return 0

    # Using binary search
    currentSmallestIndex = 0
    currentLength = lengthOfTheArray
    middle = 0
    while (currentSmallestIndex < currentLength):
        middle = (currentSmallestIndex + currentLength) // 2

        # Check if the current middle value if the value we are searching for
        if (array[middle] == target):
            return array[middle]

        # If target is smaller we search to the left half of the array
        if (target < array[middle]):
            # Check if the target is bigger than the value one position before the current middle and return it if so
            if (middle > 0 and target > array[middle - 1]):
                return array[middle - 1]
            # or else repeat for left half
            currentLength = middle

        # If target is bigger we search to the right half of the array
        else:
            # Check if the target is smaller than the value one position after the current middle and return it if so
            if (middle < lengthOfTheArray - 1 and target < array[middle + 1]):
                return array[middle]
            # or else repeat for right half
            currentSmallestIndex = middle
    return array[middle]

with open(filePath, 'r') as f:
    results = []
    numberOfValidValues = 0
    incomeColumnNumber = 0
    minimumValue = 0
    maximumValue = 0
    equiWidthRanges = []
    equiDepthRanges = []
    # read only the first line of the csv file to get the correct possition of the attribute "Income"
    for line in f:
            words = line.split(',')
            for x in range(len(words) - 1):
                if words[x] == "Income":
                    incomeColumnNumber = x
            break

    # split the values from the file and keep only the correct ones that represents income
    for line in f:
        values = line.split(',')
        tempIncomeValue = values.__getitem__(incomeColumnNumber)
        if tempIncomeValue != "" and float(tempIncomeValue) > 0:
            results.append(float(tempIncomeValue))
            numberOfValidValues += 1

    # Sorting the array of income values and storing the minimun and maximun values
    quickSort(results, 0, len(results) - 1)
    minimumValue = results[0]
    maximumValue = results[len(results) - 1]
    #results.sort()

    # Storing the number of numtuples in each case and the ranges
    equiWidthNumtuples = equiWidth(equiWidthRanges, minimumValue, maximumValue, results)
    equiDepthNumtuples = equiDepth(equiDepthRanges, minimumValue, results)

    print(str(numberOfValidValues) + " valid income values")
    print("minimum income = " + str(minimumValue) + " maximum income = " + str(maximumValue))
    print("equiwitdh: ")
    for i in range(len(equiWidthRanges)):
        if i < len(equiWidthNumtuples) :
            print("range: [" + str(equiWidthRanges[i]) + "," + str(equiWidthRanges[i+1]) + "), numtuples: " + str(equiWidthNumtuples[i]))
    for i in range(len(equiDepthRanges)):
        if i < len(equiDepthRanges) - 1:
            print("range: [" + str(equiDepthRanges[i]) + "," + str(equiDepthRanges[i+1]) + "), numtuples: " + str(equiDepthNumtuples[i]))

    # Second part

    estimatedResults = 0
    lowerLimit = float(input("Please enter the lower limit: "))
    upperLimit = float(input("Please enter the upper limit: "))

    lengthOfRanges = len(equiWidthRanges)
    lowestLimitFromRanges = findLowestLimit(equiWidthRanges, lengthOfRanges, lowerLimit)
    # The given limits ara under or over the limits we are searching for
    temp = equiWidthRanges.index(lowestLimitFromRanges) + 1
    if (upperLimit < equiWidthRanges[0] or lowestLimitFromRanges == 0):
        print("equiwidth estimated results: " + str(estimatedResults))
    # The given limits are 'inside' 1 bin
    elif (upperLimit <= equiWidthRanges[temp]):
        if lowerLimit <= equiWidthRanges[0]:
            lowerLimit = equiWidthRanges[0] ##
        percentageOfLowestBin = (equiWidthRanges[temp] - lowerLimit) / (
                    equiWidthRanges[temp] - equiWidthRanges[temp - 1])
        tuplesToEliminate = equiWidthNumtuples[temp - 1] * percentageOfLowestBin
        percentageOfHighestBin = (upperLimit - equiWidthRanges[temp - 1]) / (equiWidthRanges[temp] - equiWidthRanges[temp - 1])
        tuplesToEliminate += equiWidthNumtuples[temp - 1] * percentageOfHighestBin
        estimatedResults = tuplesToEliminate - equiWidthNumtuples[temp - 1]
        print("equiwidth estimated results: " + str(estimatedResults))
    else:
        #temp = equiWidthRanges.index(lowestLimitFromRanges) + 1
        if (lowerLimit < equiWidthRanges[0]):
            lowerLimit = equiWidthRanges[0]

        percentageOfLowestBin = (equiWidthRanges[temp] - lowerLimit) / (equiWidthRanges[temp] - equiWidthRanges[temp - 1])
        tuplesOfLowestBin = equiWidthNumtuples[temp - 1] * percentageOfLowestBin
        estimatedResults += tuplesOfLowestBin
        flag = 1
        while (upperLimit > equiWidthRanges[temp]):
            if (temp == len(equiWidthRanges) - 1):
                break
            if (flag == 0):
                estimatedResults += equiWidthNumtuples[temp - 1]
            temp += 1
            flag = 0
        # to ido pou xriazomai panw alla gia to upperLimit
        if (upperLimit > equiWidthRanges[temp]):
            upperLimit = equiWidthRanges[temp]
        percentageOfHighestBin = (upperLimit - equiWidthRanges[temp - 1]) / (equiWidthRanges[temp] - equiWidthRanges[temp - 1])
        tuplesOfHighestBin = equiWidthNumtuples[temp - 1] * percentageOfHighestBin
        estimatedResults += tuplesOfHighestBin
        print("equiwidth estimated results: " + str(estimatedResults))

    #equidepth
    estimatedResults = 0
    lengthOfRanges = len(equiDepthRanges)
    lowestLimitFromRanges = findLowestLimit(equiDepthRanges, lengthOfRanges, lowerLimit)
    # The given limits ara under or over the limits we are searching for
    temp = equiDepthRanges.index(lowestLimitFromRanges) + 1
    if (upperLimit < equiDepthRanges[0] or lowestLimitFromRanges == 0):
        print("equidepth estimated results: " + str(estimatedResults))
    elif (upperLimit <= equiDepthRanges[temp]):
        percentageOfLowestBin = (equiDepthRanges[temp] - lowerLimit) / (
                    equiDepthRanges[temp] - equiDepthRanges[temp - 1])
        tuplesToEliminate = equiDepthNumtuples[temp - 1] * percentageOfLowestBin
        percentageOfHighestBin = (upperLimit - equiDepthRanges[temp - 1]) / (equiDepthRanges[temp] - equiDepthRanges[temp - 1])
        tuplesToEliminate += equiDepthNumtuples[temp - 1] * percentageOfHighestBin
        estimatedResults = tuplesToEliminate - equiDepthNumtuples[temp - 1]
        print("equidepth estimated results: " + str(estimatedResults))
    else:
        #temp = equiDepthRanges.index(lowestLimitFromRanges) + 1
        percentageOfLowestBin = (equiDepthRanges[temp] - lowerLimit) / (
                    equiDepthRanges[temp] - equiDepthRanges[temp - 1])
        tuplesOfLowestBin = equiDepthNumtuples[temp - 1] * percentageOfLowestBin
        estimatedResults += tuplesOfLowestBin
        flag = 1
        while (upperLimit > equiDepthRanges[temp]):
            if (temp == len(equiDepthRanges) - 1):
                break
            if (flag == 0):
                estimatedResults += equiDepthNumtuples[temp - 1]
            temp += 1
            flag = 0
        #if (upperLimit > equiDepthRanges[temp]):
        #    upperLimit = equiDepthRanges[temp]
        percentageOfHighestBin = (upperLimit - equiDepthRanges[temp - 1]) / (
                    equiDepthRanges[temp] - equiDepthRanges[temp - 1])
        tuplesOfHighestBin = equiDepthNumtuples[temp - 1] * percentageOfHighestBin
        estimatedResults += tuplesOfHighestBin
        print("equidepth estimated results: " + str(estimatedResults))

    # actual results
    actualresults = 0
    for i in results:
        if i >= lowerLimit and i < upperLimit:
            actualresults += 1
    print("actual results: " + str(actualresults))