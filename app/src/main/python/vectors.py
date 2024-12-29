from math import acos
post=['i','j','k','l','m','n']
class vector:
    def __init__(self,name,magnitude,directions,vec,show):
        self.name=name
        self.magnitude=magnitude
        if vec!=[0,0,0]:
            self.length=len(directions)
        else:
            self.length=len(vec)
        self.directions=directions
        self.vec=vec
        self.show=show
    def getName(self):
        return self.name
    def getMagnitude(self):
        return self.magnitude
    def getLength(self):
        return self.length
    def getDirections(self):
        return self.directions
    def getVector(self):
        return self.vec
    def getShow(self):
        return self.show
def define(vect,name):
    v=vect.split()
    if len(v)>len(post):
        return False,[],'Dimensions should be less than 7'
    try:
        for i in range(len(v)):
            v[i]=float(v[i])
    except:
        return False,[],'Invalid Entry'

    prop=properties(v)
    show=''
    for i in range(len(v)):
        show+=str(v[i])+post[i]+' + '
    else:
        show=show[:-2]
    return True,vector(name,prop[1],prop[2],v,show),show



def properties(v):
    magnitude=getMagnitude(v)
    if magnitude==0:
        return True,0,[]
    else:
        return True,magnitude,getDirections(v,magnitude)
def getMagnitude(v):
    sq_sum=0
    for i in v:
        sq_sum+=(i**2)
    return round((sq_sum**0.5),3)
def getDirections(v,magnitude):
    directions=[]
    for i in v:
        directions.append(round((i/magnitude),3))
    return directions
def addition(a,b):
    sum=[]
    la=a.getLength()
    lb=b.getLength()
    if la>=lb:
        for i in range(lb):
            sum.append((a.getVector())[i]+(b.getVector())[i])
        else:
            sum.extend((a.getVector())[(i+1):])
    else:
        for i in range(la):
            sum.append((a.getVector())[i]+(b.getVector())[i])
        else:
            sum.extend((b.getVector())[(i+1):])
    return sum
def subtraction(a,b):
    rst=[]
    la=a.getLength()
    lb=b.getLength()
    if la>=lb:
        for i in range(lb):
            rst.append((a.getVector())[i]-(b.getVector())[i])
        else:
            rst.extend((a.getVector())[i+1:])
    else:
        for i in range(la):
            rst.append((a.getVector())[i]-(b.getVector())[i])
        else:
            for j in range(la,lb):
                rst.append((b.getVector())[j]*(-1))
    return rst
def scale(a,l):
    l=int(l)
    rst=[]
    for i in a.getVector():
        rst.append(i*l)
    return rst
def dotProduct(a,b):
    la=a.getLength()
    lb=b.getLength()
    a=(a.getVector())
    a.extend([0]*(lb-la))
    b=(b.getVector())
    b.extend([0]*(la-lb))
    rst=0
    for i in range(max(la,lb)):
        rst=rst+(a[i]*b[i])
    return rst
def crossProduct(a,b):
    '''Until 3 dimensions'''
    la=a.getLength()
    lb=b.getLength()
    if la<=3 and lb<=3:
        a=(a.getVector())
        a.extend([0]*(lb-la))
        b=(b.getVector())
        b.extend([0]*(la-lb))
        c=[1,1,1]
        matrix=[c,a,b]
        rst=detMatrix(matrix)
        return True,rst
    else:
        return False,0
def scalarTripleProduct(a,b,c):
    state,cross=crossProduct(b,c)
    if state:
        vec=''
        for i in cross:
            vec+=str(i)+" "
        s,my_cross,show=define(vec,"cross")
        return True,dotProduct(a,my_cross)
    else:
        return False,0
def vectorTripleProduct(a,b,c):
    state,cross=crossProduct(b,c)
    if state:
        vec=''
        for i in cross:
            vec+=str(i)+" "
        s,my_cross,show=define(vec,"cross")
        return crossProduct(a,my_cross)
    else:
        return False,0
def isParallel(a,b):
    la=a.getLength()
    lb=b.getLength()
    if la!=lb:
        return False
    else:
        da=a.getDirections()
        db=b.getDirections()
        fact='0'
        for i in range(la):
            if (da[i]!=0 and db[i]==0) or (da[i]!=0 and db[i]==0):
                return False
            elif da[i]==0 and db[i]==0:
                continue
            else:
                if fact=='0':
                    fact=round(da[i]/db[i],3)
                else:
                    if fact!=round(da[i]/db[i],3):
                        return False
        else:
            return True
def isPerpendicular(a,b):
    if dotProduct(a,b)==0:
        return True
    else:
        return False
# FROM MATRIX
def determinantMatrix(matrix,is_vector=False):
    if len(matrix)==1:
        fixed=matrix[0][0]
        return fixed
    else:
        total=0
        co_eff=1
        vec=[]
        for i in range(len(matrix)):
            fixed=matrix[i][0]
            sub=myCopy(matrix)
            sub.pop(i)
            for j in range(len(sub)):
                sub[j].pop(0)
                determinant=determinantMatrix(sub)
                total=total+fixed*co_eff*determinant
            co_eff=-co_eff
            if len(matrix)==3 and is_vector:
                vec.append(total)

        if is_vector:
            return vec
        else:
            return total
def detMatrix(matrix):
    i=matrix[1][1]*matrix[2][2]-matrix[2][1]*matrix[1][2]
    j=-1*(matrix[1][0]*matrix[2][2]-matrix[2][0]*matrix[1][2])
    k=matrix[1][0]*matrix[2][1]-matrix[2][0]*matrix[1][1]
    return [i,j,k]

def myCopy(matrix):
    copy=[]
    for row in matrix:
        new_row=[]
        for ele in row:
            new_row.append(ele)
        copy.append(new_row)
    return copy
def myPrint(v):
    show=''
    for i in range(len(v)):
        show+=str(v[i])+post[i]+' + '
    else:
        show=show[:-2]
    return show
def getVecName(string):
    l=string.split()
    return l[0]
    
