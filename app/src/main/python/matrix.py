def setMatrix(data):
    data=data.split('\n')
    matrix=[]
    for row in data:
        row=row.split()
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
def getOrder(matrix):
    #mxn
    m=len(matrix)
    n=len(matrix[0])
    return (m,n)
def categorize(matrix):
    details=[]
    details.append(("Square",squreMatrix(getOrder(matrix))))
    details.append(("Null",nullMatrix(matrix)))
    details.append(("Row",rowMatrix(getOrder(matrix))))
    details.append(("Column",columnMatrix(getOrder(matrix))))
    details.append(("Diagonal",diagonalMatrix(matrix)))
    details.append(("Unit",unitMatrix(matrix)))
    details.append(("Symmetric",symmetricMatrix(matrix)))
    details.append(("Skew_Symmetric",skewSymmetricMatrix(matrix)))
    details.append(("Upper",upperTriangularMatrix(matrix)))
    details.append(("Lower",lowerTriangularMatrix(matrix)))
    details.append(("Orthogonal",orthogonalMatrix(matrix)))
    return dict(details)
def squreMatrix(order):
    if order[0]==order[1]:
        return True
    else:
        return False
def nullMatrix(matrix):
    null=True
    for row in matrix:
        for ele in row:
            if ele!=0:
                null=False
                break
        if not null:
            break
    return null
def rowMatrix(order):
    if order[0]==1:
        return True
    else:
        return False
def columnMatrix(order):
    if order[1]==1:
        return True
    else:
        return False
def diagonalMatrix(matrix):
    if squreMatrix(getOrder(matrix)):
        diagonal=True
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if i!=j and matrix[i][j]!=0:
                    diagonal=False
                    break
            if not diagonal:
                break
        return diagonal
    else:
        return False
def unitMatrix(matrix):
    unit=True
    if diagonalMatrix(matrix):
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if i==j and matrix[i][j]!=1:
                    unit=False
                    break
            if not unit:
                break
    else:
        unit=False
    return unit
def symmetricMatrix(matrix):
    if squreMatrix(getOrder(matrix)):
        symmetric=True
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j]!=matrix[j][i]:
                    symmetric=False
                    break
            if symmetric:
                break
        return symmetric
    else:
        return False
def skewSymmetricMatrix(matrix):
    if squreMatrix(getOrder(matrix)):
        skew_symmetric=True
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j]!=-1*matrix[j][i]:
                    skew_symmetric=False
                    break
            if not skew_symmetric:
                break
        return skew_symmetric
    else:
        return False
def upperTriangularMatrix(matrix):
    if squreMatrix(getOrder(matrix)):
        upper=True
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j]!=0 and j<i:
                    upper=False
                    return upper
        else:
            return upper
    else:
        return False
def lowerTriangularMatrix(matrix):
    if squreMatrix(getOrder(matrix)):
        lower=True
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j]!=0 and j>i:
                    lower=False
                    break
            if not lower:
                break
        return lower
    else:
        return False
def equlantMatrix(A,B):
    if A==B:
        return True
    else:
        return False
def orthogonalMatrix(matrix):
    v1,transpose=transposeMatrix(matrix)
    v2,multipication=multipicationMatrix(matrix,transpose)
    v3=unitMatrix(multipication)
    if v3:
        return True                
    else:
        return False
def addition(A,B):
    orderA=getOrder(A)
    orderB=getOrder(B)
    result=[]
    if orderA==orderB:
        for i in range(orderA[0]):
            row=[]
            for j in range(orderA[1]):
                add=A[i][j]+B[i][j]
                row.append(add)
            result.append(row)
        return True,result
    else:
        return False,result            
def substraction(A,B):
    orderA=getOrder(A)
    orderB=getOrder(B)
    result=[]
    if orderA==orderB:
        for i in range(orderA[0]):
            row=[]
            for j in range(orderA[1]):
                sub=A[i][j]-B[i][j]
                row.append(sub)
            result.append(row)
        return True,result
    else:
        return False,result
def scalarMutipication(matrix,scalar):
    try:
        if '.' in scalar:
            scalar=float(scalar)
        else:
            scalar=int(scalar)
    except:
        return False,0
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            matrix[i][j]=scalar*matrix[i][j]
    return True,matrix
def transposeMatrix(matrix):
    result=[]
    for j in range(len(matrix[0])):
        row=[]
        for i in range(len(matrix)):
             row.append(matrix[i][j])
        result.append(row)
    return True,result
def multipicationMatrix(A,B):
    orderA=getOrder(A)
    orderB=getOrder(B)
    result=[]
    if orderA[1]==orderB[0]:
        valid,B=transposeMatrix(B)
        if valid:
            for i in range(orderA[0]):
                row=[]
                for j in  range(orderB[1]):
                    total=0
                    for k in range(orderA[1]):
                        multi=A[i][k]*B[j][k]
                        total=total+multi
                    row.append(total)
                result.append(row)
            return True,result
        else:
            return False,result                    
    else:
        return False,result
def determinantMatrix(matrix):
    order=getOrder(matrix)
    if squreMatrix(order):
        if order==(1,1):
            fixed=matrix[0][0]
            return True,fixed
        else:
            total=0
            co_eff=1
            for i in range(len(matrix)):
                fixed=matrix[i][0]
                sub=myCopy(matrix)
                sub.pop(i)
                for j in range(len(sub)):
                    sub[j].pop(0)
                    v,determinant=determinantMatrix(sub)
                total=total+fixed*co_eff*determinant
                co_eff=-co_eff
            return True,total
    else:
        return False,0
def findCo_FactorMatrix(matrix):
    co_factor_matrix=[]
    for x in range(len(matrix)):
        row=[]
        for y in range(len(matrix[0])):
            sub=myCopy(matrix)
            sub.pop(x)
            for j in range(len(sub)):
                sub[j].pop(y)
            v,minor=determinantMatrix(sub)
            co_factor=(-1)**(x+y)
            row.append(co_factor*minor)
        co_factor_matrix.append(row)
    return True,co_factor_matrix
def inverseMatrix(matrix):
    inverse=[]
    v,determinant=determinantMatrix(matrix)
    if v and determinant!=0:
        v1,co_factor_matrix=findCo_FactorMatrix(matrix)
        v2,adjoint=transposeMatrix(co_factor_matrix)
        determinant=1/determinant
        v3,inverse=scalarMutipication(adjoint,str(determinant))
        return True,inverse
    else:
        return False,inverse
def myCopy(matrix):
    copy=[]
    for row in matrix:
        new_row=[]
        for ele in row:
            new_row.append(ele)
        copy.append(new_row)
    return copy
def myPrint(matrix):
    line=""
    for row in matrix:
        for ele in row:
            if type(ele)==float:
                ele=round(ele,3)
            line=line+str(ele)+"\t\t\t\t"
        line=line+"\n"
    return line
    
