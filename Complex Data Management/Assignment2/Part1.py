#Vasilis Nikas
#AM: 3143

filePath = 'tiger_roads.csv'

def calculateGridAxis(min, max):
    cellsCoordinates = []

    for x in range(11):
        cellsCoordinates.append(min + ((x*(max - min))/10))
    return cellsCoordinates

def fillingCells(records, gridXAxisCoordinates, gridYAxisCoordinates):
    cellsValues = {}
    tempCell = []
    currentMbrMaxX = []
    currentMbrMaxY = []
    currentMbrMinX = []
    currentMbrMinY = []

    minmaxmbr = records[1::3]
    for x in range(len(minmaxmbr)):
        currentMbrMaxX.append(minmaxmbr[x][1][0])
        currentMbrMaxY.append(minmaxmbr[x][1][1])
        currentMbrMinX.append(minmaxmbr[x][0][0])
        currentMbrMinY.append(minmaxmbr[x][0][1])
    for i in range(10):
        for j in range(10):
            cellsValues[str(i)+str(j)] = []

    for x in range(len(minmaxmbr)):
        # These are for cells only (0,0)
        miny = minx = maxy = maxx = 0
        flag = flag1 = flag2 = flag3 = 0
        for j in range(11):
            if (currentMbrMinX[x] <= gridXAxisCoordinates[j] and flag == 0):
                if j == 0:
                    minx = 0
                else:
                    minx = j - 1
                flag = 1
            if (currentMbrMinY[x] <= gridYAxisCoordinates[j] and flag1 == 0):
                if j == 0:
                    miny = 0
                else:
                    miny = j - 1
                flag1 = 1
            if (currentMbrMaxX[x] <= gridXAxisCoordinates[j] and flag2 == 0):
                maxx = j - 1
                flag2 = 1
            if (currentMbrMaxY[x] <= gridYAxisCoordinates[j] and flag3 == 0):
                maxy = j - 1
                flag3 = 1
        xrange = maxx - minx
        yrange = maxy - miny
        for i in range(xrange+1):
            for j in range(yrange+1):
                cellsValues[str(minx+i) + str(miny+j)].append(x+1)
    return cellsValues

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


def makeFile(cellsValues, records, minX, minY, maxX, maxY):
    grd = open("grid.grd", 'w')
    grid = open("grid.dir", "w")
    ids = records[0::3]
    #minmaxmbr = records[1::3]
    #xy = records[2::3]
    grid.write(str(1) + " " + str(minX) + " " + str(maxX) + " " + str(minY) + " " + str(maxY) + "\n")
    counter = 2
    overallList = []
    idsDict = dict(zip(ids, range(0, len(ids))))
    
    for x in cellsValues:
        templist = cellsValues[x]
        overallList.extend(templist)
        grid.write(str(counter) + " " + str(x[0]) + " " + str(x[1]) + " " + str(len(cellsValues[x])) + "\n")
        counter += 1

    #overallList = tuple(overallList)
    for i in overallList:
        target = findLowestLimit(ids,len(ids),i)
        if (i == target):
            j = idsDict[i]
            temp = records[j*3+2][:]
            #grd.write(str(ids[j]) + "," + str(minmaxmbr[j][0][0]) + " " + str(minmaxmbr[j][0][1]) + "," + str(minmaxmbr[j][1][0]) + " " + str(minmaxmbr[j][1][1]) + ",") #+ str(xy[j][:][:][0]) + "\n")
            grd.write(str(ids[j]) + "," + str(records[j*3+1][0][0]) + " " + str(records[j*3+1][0][1]) + "," + str(records[j*3+1][1][0]) + " " + str(records[j*3+1][1][1]) + ",")
            for v in temp:
                grd.write(str(v[0]) + " " + str(v[1]) + ",")
            grd.write("\n")
    grd.close()
    grid.close()

with open(filePath, 'r') as f:
    records = []
    idCounter = 1
    maxX = -9999
    minY = 9999
    maxY = minX = 0

    for line in f:
        break
    for line in f:
        linestring = []
        mbrMinX = mbrMaxY = 0
        mbrMaxX = -9999
        mbrMinY = 9999
        mbrMins = []
        mbrMaxs = []
        mbr = []
        coordinates = line.split(',')
        for x in range(len(coordinates)):
            coordinate = coordinates[x].split(' ')
            coordinate[0] = float(coordinate[0])
            coordinate[1] = float(coordinate[1])
            linestring.append(coordinate)
            if mbrMinX > coordinate[0]:
                mbrMinX = coordinate[0]
                if minX > mbrMinX:
                    minX = mbrMinX
            if mbrMaxX < coordinate[0]:
                mbrMaxX = coordinate[0]
                if maxX < mbrMaxX:
                    maxX = mbrMaxX
            if mbrMinY > coordinate[1]:
                mbrMinY = coordinate[1]
                if minY > mbrMinY:
                    minY = mbrMinY
            if mbrMaxY < coordinate[1]:
                mbrMaxY = coordinate[1]
                if maxY < mbrMaxY:
                    maxY = mbrMaxY
        # Appending in a list the current mbrs max and min
        mbrMins.append(mbrMinX)
        mbrMins.append(mbrMinY)
        mbrMaxs.append(mbrMaxX)
        mbrMaxs.append(mbrMaxY)
        mbr.append(mbrMins)
        mbr.append(mbrMaxs)
        # Appending in the records list each record
        records.append(idCounter)
        records.append(mbr)
        records.append(linestring)
        idCounter += 1
        #break
    # Constructing the grid
    gridXAxisCoordinates = calculateGridAxis(minX,maxX)
    gridYAxisCoordinates = calculateGridAxis(minY, maxY)
    cellValues = fillingCells(records, gridXAxisCoordinates, gridYAxisCoordinates)
    makeFile(cellValues,records,minX,minY,maxX,maxY)