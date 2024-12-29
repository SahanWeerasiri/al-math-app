matrix=[]
def createMatrix(data):
    data=data.split('\n')
    matrix=[]
    while '' in data:
        data.remove('')
    for row in data:
        row=row.split()
        while '' in row:
            row.remove('')
        matrix.append(row)
    valid,matrix=checkValidity(matrix)
    return valid,matrix
def checkValidity(matrix):
    v1=checkLengths(matrix)
    v2=checkAvailability(matrix)
    v3,matrix=convertElements(matrix)
    return all((v1,v2,v3)),matrix
def checkLengths(matrix):
    valid=False
    length=len(matrix[0])
    for row in matrix:
        if len(row)!=length:
            break
    else:
        valid=True
    return valid
def checkAvailability(matrix):
    try:
        if matrix[0][0] != None:
            valid=True
        else:
            valid=False
    except:
        valid=False
    return valid
def convertElements(matrix):
    valid=True
    new_matrix=[]
    try:
        for row in matrix:
            new_row=[]
            for ele in row:
                if '.' in ele:
                    ele=float(ele)
                else:
                    ele=int(ele)
                new_row.append(ele)
            new_matrix.append(new_row)
    except:
        valid=False
    return valid,new_matrix
def getREF(pointer):
    global matrix
    if pointer==len(matrix)-1:
        if matrix[pointer][pointer]!=0:
            temp_row=matrix[pointer].copy()
            matrix[pointer]=scaleRow(temp_row,1/matrix[pointer][pointer])
            return
        else:
            return
    if matrix[pointer][pointer]!=0:
        temp_row=matrix[pointer].copy()
        matrix[pointer]=scaleRow(temp_row,1/matrix[pointer][pointer])
        for i in range(pointer+1,len(matrix)):
            temp_row1,temp_row2=matrix[i].copy(),matrix[pointer].copy()
            matrix[i]=addRows(temp_row1,scaleRow(temp_row2,-matrix[i][pointer]))
        getREF(pointer+1)
        return
    else:
        getREF(pointer+1)
        return
def addRows(r1,r2):
    for i in range(len(r1)):
        r1[i]=r1[i]+r2[i]
    return r1
def scaleRow(r,s):
    for i in range(len(r)):
        r[i]=r[i]*s
    return r
def getRREF(pointer):
    global matrix
    #pointer from bottom
    if pointer==0:
        return
    while pointer>0:
        for i in range(pointer):
            temp_row1,temp_row2=matrix[i].copy(),matrix[pointer].copy()
            matrix[i]=addRows(temp_row1,scaleRow(temp_row2,-matrix[i][pointer]))
        pointer=pointer-1
    return
def giveResults():
    show=''
    for i in range(len(matrix)):
        show=show+'x'+str(i+1)+' = '+str(round(matrix[i][-1],3))+'\n'
    return show
def main(data,is_matrix_created=0):
    global matrix
    state,matrix=createMatrix(data)
    if state:
        getREF(0)
        state,msg=getRank()
        if state:
            getRREF(len(matrix)-1)
            if is_matrix_created==0:
                return True,giveResults()
            else:
                p=[]
                for i in range(len(matrix)):
                    p.append(round(matrix[i][-1],3))
                return True,p
        else:
            matrix=[]
            return False,msg
    else:
        matrix=[]
        return False,'Invalid equations'
def getRank():
    #after getting REF
    n=len(matrix[0])-1
    rank_A=0
    for row in matrix:
        if isNonZeroRow(row[:-1]):
            rank_A+=1
    rank_A_B=0
    for row in matrix:
        if isNonZeroRow(row):
            rank_A_B+=1
    if rank_A==rank_A_B==n:
        return True,'OK'
    elif rank_A==rank_A_B<n:
        return False,'Infinitely many solutions'
    elif rank_A<rank_A_B:
        return False,'No solutions'
    else:
        return False,'Default'
def isNonZeroRow(row):
    zero_count=0
    for ele in row:
        if ele==0:
            zero_count+=1
    if zero_count==len(row):
        return False
    else:
        return True
        
