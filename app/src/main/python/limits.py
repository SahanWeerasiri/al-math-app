from math import *
def rearrange(y):
    valid=True
    allow={'x','+','-','*','/','^','(',')','0','1','2','3','4','5','6','7','8','9'}
    allow2={'sin','cos','tan'}
    y_copy=y
    for i in allow2:
        if i in y_copy:
            y_copy=y_copy.replace(i,'')
    for item in y_copy:
        if item not in allow:
            valid=False
            return valid,y_copy
    else:
        y=y.replace('^','**')
        return valid,y
def getAllows():
    allow2=['sin','cos','tan']
    return allow2
def f(y,p,d):
    try:
        l=0
        left=eval(y.replace('x',str(p-d)))
        l=1
        right=eval(y.replace('x',str(p+d)))
        return 1,left,right
    except ZeroDivisionError as e:
        d=d/5
        if l==0:
            left=eval(y.replace('x',str(p-d)))
            right=eval(y.replace('x',str(p+d)))
            return 1,left,right
        else:
            right=eval(y.replace('x',str(p+d)))
            return 1,left,right
    except:
        return 0,0,0
#y=input('Enter the function : ')
#point=input('Point : ')
def getLimit(y,point):
    valid_point=True
    try:
        if '.' in point:
            point=float(point)
        else:
            point=int(point)
    except:
        valid_point=False
    if valid_point:
        state,y=rearrange(y)
        delta=1
        if state:
            state,left,right=f(y,point,delta)
            count=1
            while state==1 and count!=10:
                delta=delta/5
                state,left,right=f(y,point,delta)
                count=count+1
            else:
                if round(left,3)!=round(right,3):
                    state=2
                else:
                    state=1
            if state==2:
                #print('No limt at',point)
                return 2,0
            elif state==0:
                #print('Error in function')
                return 0,0
            else:
                #print('limit is',round(left,3))
                return 1,round(left,3)
        else:
            #print('Invalid function')
            return 3,0
    else:
        #print('Invalid point')
        return 4,0
