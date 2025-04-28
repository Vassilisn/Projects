#Vasilis Nikas
#AM: 3143

grdFilePath = 'grid.grd'
dirFilePath = 'grid.dir'
queryPath = 'queries.txt'

def calculateGridAxis(min, max):
    cellsCoordinates = []
    for x in range(11):
        cellsCoordinates.append(min + ((x*(max - min))/10))
    return cellsCoordinates

def checkCellsForWindow(windowCoordinates, gridXAxisCoordinates, gridYAxisCoordinates):
    windowCells = []
    #miny = minx = maxy = maxx = 0
    miny = 0
    maxy = 9
    minx = 0
    maxx = 9
    flag = flag1 = flag2 = flag3 = 0
    for j in range(len(gridXAxisCoordinates)):
        if (windowCoordinates[0] <= gridXAxisCoordinates[j] and flag == 0):
            if j == 0:
                minx = 0
            else:
                minx = j - 1
            flag = 1
        if (windowCoordinates[2] <= gridYAxisCoordinates[j] and flag1 == 0):
            if j == 0:
                miny = 0
            else:
                miny = j - 1
            flag1 = 1
        if (windowCoordinates[1] <= gridXAxisCoordinates[j] and flag2 == 0):
            maxx = j - 1
            flag2 = 1
        if (windowCoordinates[3] <= gridYAxisCoordinates[j] and flag3 == 0):
            maxy = j - 1
            flag3 = 1
    xrange = maxx - minx
    yrange = maxy - miny
    for i in range(xrange + 1):
        for j in range(yrange + 1):
            windowCells.append(str(minx + i) + str(miny + j))
    return windowCells

def findMbr(mbrsId, records):
    ids = records[0::3]
    mbr = []
    for x in ids:
        if (mbrsId == x):
            indexer = ids.index(x)
            mbr.append(records[indexer*3 + 1][0][0])
            mbr.append(records[indexer*3 + 1][1][0])
            mbr.append(records[indexer*3 + 1][0][1])
            mbr.append(records[indexer*3 + 1][1][1])
    return mbr

def findIntersection(windowCells, windowCoordinates, cellValues, records, gridXAxisCoordinates, gridYAxisCoordinates, emptyCells):
    referencePoint = []
    mbrsForComparison = []
    intersectMbrs = []
    emptyCellsCounter = 0

    ids = records[0::3]
    idsDict = dict(zip(ids, range(0, len(ids))))

    for i in windowCells:
        cells = [int(x) for x in i]
        if (cellValues[i] == []):
            emptyCellsCounter += 1
        if (i in cellValues):
            mbrsForComparison.extend(cellValues[i])

        for mbrsId in mbrsForComparison:
            #mbrCoordinates = findMbr(mbrsId, records)
            mbrCoordinates = []
            # mbrCoordinates = findMbr(mbrsId, records)
            indexer = idsDict[mbrsId]
            mbrCoordinates.append(records[indexer * 3 + 1][0][0])
            mbrCoordinates.append(records[indexer * 3 + 1][1][0])
            mbrCoordinates.append(records[indexer * 3 + 1][0][1])
            mbrCoordinates.append(records[indexer * 3 + 1][1][1])

            if (windowCoordinates[0] < mbrCoordinates[0]):
                referencePoint.append(mbrCoordinates[0])
            else:
                referencePoint.append(windowCoordinates[0])
            if (windowCoordinates[2] < mbrCoordinates[2]):
                referencePoint.append(mbrCoordinates[2])
            else:
                referencePoint.append(windowCoordinates[2])

            if (windowCoordinates[0] <= mbrCoordinates[1] and windowCoordinates[2] <= mbrCoordinates[3] and windowCoordinates[1] >= mbrCoordinates[0] and windowCoordinates[3] >= mbrCoordinates[2]):
                if ((referencePoint[0] <= gridXAxisCoordinates[cells[0]+1] and referencePoint[0] >= gridXAxisCoordinates[cells[0]]) or (referencePoint[1] <= gridYAxisCoordinates[cells[0]+1] and referencePoint[1] >= gridYAxisCoordinates[cells[0]])):
                    intersectMbrs.append(mbrsId)
            referencePoint = []
        mbrsForComparison = []
    """
    for i in intersectMbrs:
        print(i , end = " ")
    print()
    print("Cells: " + str(len(windowCells) - emptyCellsCounter))
    print("Results: " + str(len(intersectMbrs)))
    print("----------")"""
    emptyCells.append(emptyCellsCounter)
    return intersectMbrs

def findLineString(mbrsId, records):
    ids = records[0::3]
    lineString = []
    for x in ids:
        if (mbrsId == x):
            indexer = ids.index(x)
            lineString = records[indexer*3 + 2]
    return lineString

def findRefinementStage(intersectMbrs, windowCoordinates, records, emptyCells):
    refinementMbrs = []
    ids = records[0::3]
    idsDict = dict(zip(ids, range(0, len(ids))))
    for i in range(len(intersectMbrs)):
        #mbrCoordinates = findMbr(intersectMbrs[i], records)
        mbrCoordinates = []
        # mbrCoordinates = findMbr(mbrsId, records)
        indexer = idsDict[intersectMbrs[i]]
        mbrCoordinates.append(records[indexer * 3 + 1][0][0])
        mbrCoordinates.append(records[indexer * 3 + 1][1][0])
        mbrCoordinates.append(records[indexer * 3 + 1][0][1])
        mbrCoordinates.append(records[indexer * 3 + 1][1][1])
        
        if (mbrCoordinates[0] >= windowCoordinates[0] and mbrCoordinates[1] <= windowCoordinates[1]):
            if (mbrCoordinates[3] >= windowCoordinates[2] and mbrCoordinates[2] <= windowCoordinates[3]):
                if (intersectMbrs[i] not in refinementMbrs):
                    refinementMbrs.append(intersectMbrs[i])
                #refinementMbrs.append(intersectMbrs[i])
        elif (mbrCoordinates[2] >= windowCoordinates[2] and mbrCoordinates[3] <= windowCoordinates[3]):
            if (mbrCoordinates[2] >= windowCoordinates[0] and mbrCoordinates[0] <= windowCoordinates[1]):
                if (intersectMbrs[i] not in refinementMbrs):
                    refinementMbrs.append(intersectMbrs[i])
                #refinementMbrs.append(intersectMbrs[i])
        else:
            #lineString = findLineString(intersectMbrs[i], records)
            indexer = idsDict[intersectMbrs[i]]
            lineString = records[indexer*3 + 2]
            for line in range(len(lineString)-1):
                x1 = lineString[line][0]
                x2 = lineString[line + 1][0]
                y1 = lineString[line][1]
                y2 = lineString[line + 1][1]
                for sides in range(4):
                    if (sides == 0):
                        x3 = windowCoordinates[0]
                        x4 = windowCoordinates[1]
                        y3 = windowCoordinates[2]
                        y4 = windowCoordinates[2]
                    elif (sides == 1):
                        x3 = windowCoordinates[0]
                        x4 = windowCoordinates[1]
                        y3 = windowCoordinates[3]
                        y4 = windowCoordinates[3]
                    elif (sides == 2):
                        x3 = windowCoordinates[0]
                        x4 = windowCoordinates[0]
                        y3 = windowCoordinates[2]
                        y4 = windowCoordinates[3]
                    else:
                        x3 = windowCoordinates[1]
                        x4 = windowCoordinates[1]
                        y3 = windowCoordinates[2]
                        y4 = windowCoordinates[3]
                    # For some reason python thinks this is equal to 0 lol
                    if ((((x1-x2)*(y3-y4))-((y1-y2)*(x3-x4))) != 0):
                        t = (((x1-x3)*(y3-y4))-((y1-y3)*(x3-x4)))/(((x1-x2)*(y3-y4))-((y1-y2)*(x3-x4)))
                    if ((((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4))) != 0):
                        u = (((x1 - x3) * (y1 - y2)) - ((y1 - y3) * (x1 - x2))) / (((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4)))
                    if (t >= 0 and t <= 1 and u >= 0 and u <= 1):
                        if (intersectMbrs[i] not in refinementMbrs):
                            refinementMbrs.append(intersectMbrs[i])

    for i in refinementMbrs:
        print(i , end = " ")
    print()
    print("Cells: " + str(len(windowCells) - emptyCells[0]))
    print("Results: " + str(len(refinementMbrs)))
    print("----------")


dirFile = open(dirFilePath, "r")
grdFile = open(grdFilePath, "r")
queryFile = open(queryPath, "r")
maxX = maxY = minX = minY = 0
coordinatesOfCells = []
setOfEachCell = []
records = []
cellValues = {}
# Read the first line of dir file to get min, max of X Y
for firstline in dirFile:
    values = firstline.split(" ")
    minX = float(values[1])
    maxX = float(values[2])
    minY = float(values[3])
    maxY = float(values[4])
    break
# Store the coordinates of each Cell and store the multitude of each cell
for line in dirFile:
    values = line.split(" ")
    coordinatesOfCells.append(str(values[1]) + str(values[2]))
    setOfEachCell.append(int(values[3]))
# Read grd file
for eachRecord in grdFile:
    mbrCoordinates = []
    linestring = []
    recordValues = eachRecord.split(",")
    # Storing first the ID
    records.append(int(recordValues[0]))
    # Storing the list of mins and maxs coordinates of mbr
    minsmbr = recordValues[1].split(" ")
    minsmbr[0] = float(minsmbr[0])
    minsmbr[1] = float(minsmbr[1])
    mbrCoordinates.append(minsmbr)
    maxsmbr = recordValues[2].split(" ")
    maxsmbr[0] = float(maxsmbr[0])
    maxsmbr[1] = float(maxsmbr[1])
    mbrCoordinates.append(maxsmbr)
    records.append(mbrCoordinates)
    for x in range(len(recordValues)-1):
        if (x > 2):
            lines = recordValues[x].split(" ")
            lines[0] = float(lines[0])
            lines[1] = float(lines[1])
            linestring.append(lines)
    records.append(linestring)
# Making the dictionary for the cells
counter = 0
for x in range(len(setOfEachCell)):
    cellValues[coordinatesOfCells[x]] = []
    y = setOfEachCell[x]
    if (setOfEachCell[x] > 0):
        while (y > 0):
            cellValues[coordinatesOfCells[x]].append(records[counter*3])
            counter += 1
            y -= 1
dirFile.close()
grdFile.close()
# Reading the Query file
numberOfQuery = 0
minXWindow = minYWindow = maxXWindow = maxYWindow = 0
windowCoordinates = []
gridXAxisCoordinates = calculateGridAxis(minX, maxX)
gridYAxisCoordinates = calculateGridAxis(minY, maxY)
for line in queryFile:
    emptyCells = []
    values = line.split(",")
    numberOfQuery = int(values[0])
    coord = values[1].split(" ")
    minXWindow = float(coord[0])
    maxXWindow = float(coord[1])
    minYWindow = float(coord[2])
    maxYWindow = float(coord[3])
    windowCoordinates.append(minXWindow)
    windowCoordinates.append(maxXWindow)
    windowCoordinates.append(minYWindow)
    windowCoordinates.append(maxYWindow)
    print("Query " + str(numberOfQuery) + " results: ")
    windowCells = checkCellsForWindow(windowCoordinates, gridXAxisCoordinates, gridYAxisCoordinates)
    intersectMbrs = findIntersection(windowCells, windowCoordinates, cellValues, records, gridXAxisCoordinates, gridYAxisCoordinates,emptyCells)
    findRefinementStage(intersectMbrs, windowCoordinates, records, emptyCells)
    windowCoordinates = []




