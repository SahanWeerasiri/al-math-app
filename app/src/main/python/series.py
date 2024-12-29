ur=''
sn=''
infinite=1000
def isSymmetric(n):
    d=n[1]-n[0]
    for i in range(len(n)-1):
        if (n[i+1]-n[i])!=d:
            return False
    else:
        return True
def isGeometric(n):
    for t in n:
        if t==0:
            return False
    r=n[1]/n[0]
    for i in range(len(n)-1):
        if (n[i+1]/n[i])!=r:
            return False
    else:
        return True
def validationOfData(n):
    List=[]
    try:
        for t in n:
            List.append(eval(t))
        else:
            return True,List
    except:
        return False,List
def checkUr(ur):
    try:
        x=eval(ur.replace('r','1'))
        y=eval(ur.replace('r','2'))
        z=eval(ur.replace('r','3'))
    except:
        return False
    return True
        
def createUrSn(n):
    global ur,sn
    if checkUr(n):
        ur=n
        n=[eval(ur.replace('r',str(1))),eval(ur.replace('r',str(2))),eval(ur.replace('r',str(3)))]
        if isSymmetric(n):
            a=n[0]
            d=n[1]-n[0]
            ur=str(a)+'+'+str(d)+'*(r-1)'
            sn='n/2*(2*'+str(a)+'+(n-1)*'+str(d)+')'
            return True,3,ur,'Symmetric',a,d,sn
        elif isGeometric(n):
            a=n[0]
            r=n[1]/n[0]
            ur=str(a)+'*'+str(r)+'**(r-1)'
            sn=str(a)+'*(1-'+str(r)+'**n)/(1-'+str(r)+')'
            return True,4,ur,'Geometric',a,r,sn
        else:
            sn=''
            return True,5,ur,'This is not Symmetric or Geometric. Insert the Ur.'
    else:
        n=n.split()
        if len(n)<3:
            return False,1,'number of first terms should be more than 2'
        state,n=validationOfData(n)
        if not state:
            return False,2,'Invalid data'
        if isSymmetric(n):
            a=n[0]
            d=n[1]-n[0]
            ur=str(a)+'+'+str(d)+'*(r-1)'
            sn='n/2*(2*'+str(a)+'+(n-1)*'+str(d)+')'
            return True,3,ur,'Symmetric',a,d,sn
        elif isGeometric(n):
            a=n[0]
            r=n[1]/n[0]
            ur=str(a)+'*'+str(r)+'**(r-1)'
            sn=str(a)+'*(1-'+str(r)+'**n)/(1-'+str(r)+')'
            return True,4,ur,'Geometric',a,r,sn
        else:
            ur=''
            sn=''
            return False,5,'This is not Symmetric or Geometric. Insert the Ur.'
def getTerm(r):
    global ur
    if ur!='':
        return True,str(round(eval(ur.replace('r',str(r))),3))
    else:
        return False,""
def seriesValue(n):
    global sn,infinite,ur
    if ur=='':
        return False,0,""
    if n=='-':
        #infinite
        n=infinite
    else:
        if int(n)<=0:
            return False,1,"Details"
    if sn=='':
        n=int(n)
        total=0
        for i in range(1,n+1):
            total+=eval(ur.replace('r',str(i)))
    else:
        total=eval(sn.replace('n',str(n)))
    return True,total,isCovergent()
def isCovergent():
    state,total=fromLimits()
    if state:
        if total<1:
            return True,'Convergent to '+str(total)
        else:
            return False,2,'Divergent'
    else:
        return False,3,'Can not tell'
def fromLimits():
    global ur,infinite
    count=0
    delta=0.1
    while count!=10:
        delta=delta/5
        ar1=eval(ur.replace('r',str(infinite+1+delta)))
        ar=eval(ur.replace('r',str(infinite+delta)))
        left=round(ar1/ar,5)
        ar1=eval(ur.replace('r',str(infinite+1-delta)))
        ar=eval(ur.replace('r',str(infinite-delta)))
        right=round(ar1/ar,5)
        if left==right:
            return True,left
        count=count+1
    else:
        return False,(left,right)
