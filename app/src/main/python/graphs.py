#import matplotlib.pyplot as plt
def equationCreator(input_str):
    operators={'+','-','*','/','(',')','=',' '}
    numbers={'0','1','2','3','4','5','6','7','8','9','x','y'}
    if 'y' in input_str and '=' in input_str:
        i=0
        while i <(len(input_str)):
            ch=input_str[i]
            if ch in operators or ch in numbers:
                i+=1
            elif ch=='^':
                input_str=input_str.replace('^','**')
                i=0
            else:
                return False,input_str
        else:
            input_str=input_str.replace('y','')
            input_str=input_str.replace('=','')
            input_str=input_str.replace(' ','')
            return True,input_str
    else:
        return False,input_str
def getRange(start,end):
    try:
        start=float(start)
        end=float(end)
        if start>=end:
            return False,(0,0)
        else:
            return True,(start,end)
    except:
        return False,(0,0)
def makeCoordinates(equation,start,end,sensitivity=0.05):
    X=[]
    Y=[]
    x=start
    while x<=end:
        try:
            y=eval(equation.replace('x','('+str(x)+')'))
            y=round(y,2)
            Y.append(y)
            X.append(x)
        except:
            pass

        x=round(x+sensitivity,2)
    return True,X,Y
def tableValues(equation,start,end):
    table=[]
    state,X,Y=makeCoordinates(equation,start,end,(end-start)/10)
    if state and len(X)>0 and len(Y)>0:
        for i in range(len(X)):
            table.append([X[i],Y[i]])
        return True,table
    return False,"Type the equation correctly"
def plotGraph(X,Y,equation):
    plt.plot(X,Y,color='blue',label='y='+equation)
    plt.xlabel('X')
    plt.ylabel('Y')
    if (max(X)-min(X))>50:
        plt.xlim(-20,20)
    if (max(Y)-min(Y))>50:
        plt.ylim(-20,20)
    plt.legend()
    plt.title('Graph')
    plt.grid()
    plt.savefig('graph.jpg')
    return True,'graph.jpg'
def graphCreator(input_str,start,end):
    state,equation=equationCreator(input_str)
    if state:
        state,(start,end)=getRange(start,end)
        if not state:
            return False,[],'Invalid Range'
    else:
        return False,[],'Invalid Equation'
    #state,X,Y=makeCoordinates(equation,start,end,(end-start)/10)
    state,table=tableValues(equation,start,end)
    #state,name=plotGraph(X,Y,equation)
    if state:
        #return state,table,[X,Y]
        return state,table
    else:
        #return state,[],table
        return state,[],table
def getMaxMin(X,Y):
    return max(X),max(Y),min(X),min(Y)