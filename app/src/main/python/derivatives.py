from math import sin,cos,tan,asin,acos,atan,log,log10,exp
function=''
dx=0.001
available=['exp()','log()','log10()','sin()','cos()','tan()','asin()','acos()','atan()','0','1','2','3','4','5','6','7','8','9','x','+','-','*','/','^',' ','(',')']
valid=['exp()','log()','log10()','sin()','cos()','tan()','asin()','acos()','atan()','digits','floats','x','+,-,/,*,^,()','space']
def setFunction(string):
    global function,available
    changed=string.replace('sin','')
    changed=changed.replace('cos','')
    changed=changed.replace('tan','')
    changed=changed.replace('asin','')
    changed=changed.replace('acos','')
    changed=changed.replace('atan','')
    changed=changed.replace('log','')
    changed=changed.replace('log10','')
    changed=changed.replace('exp','')
    for ch in changed:
        if ch not in available:
            function=''
            return False
    else:
        function=string.replace('^','**')
        return True
def getAvailable():
    return valid
def f(x):
    global function
    try:
        if 'exp' in function:
            inter=function.replace('exp','_')
            inter=inter.replace('x',str(x))
            rst=eval(inter.replace('_','exp'))
            return True,rst
        rst=eval(function.replace('x',str(x)))
        return True,rst
    except:
        return False,0
def dy(x):
    s1,y=f(x)
    s2,y_dy=f(x+dx)
    if s1 and s2:
        rst=(y_dy-y)/dx
        return True,round(rst,2)
    else:
        return False,0
def d2y(x):
    s1,y=f(x)
    s2,y_dy=f(x+dx)
    s3,y_2dy=f(x+2*dx)
    if s1 and s2 and s3:
        rst=(y_2dy-2*y_dy+y)/(dx**2)
        return True,round(rst,2)
    else:
        return False,0
def d3y(x):
    s1,y=f(x)
    s2,y_dy=f(x+dx)
    s3,y_2dy=f(x+2*dx)
    s4,y_3dy=f(x+3*dx)
    if s1 and s2 and s3 and s4:
        rst=(y_3dy-3*y_2dy+3*y_dy-y)/(dx**3)
        return True,round(rst,2)
    else:
        return False,0
def d4y(x):
    s1,y=f(x)
    s2,y_dy=f(x+dx)
    s3,y_2dy=f(x+2*dx)
    s4,y_3dy=f(x+3*dx)
    s5,y_4dy=f(x+4*dx)
    if s1 and s2 and s3 and s4 and s5:
        rst=(y_4dy-4*y_3dy+6*y_2dy-4*y_dy+y)/(dx**4)
        return True,round(rst,2)
    else:
        return False,0
def derivatives(n,x):
    try:
        n=int(n)
    except:
        return False,0
    if n==1:
        return dy(x)
    elif n==2:
        return d2y(x)
    elif n==3:
        return d3y(x)
    elif n==4:
        return d4y(x)
    elif n==0:
        ans=f(x)
        return ans[0],round(ans[1],2)
    else:
        return False,"Out of order"
def integrating(start,end):
    global function
    dx=0.0005
    try:
        total=0
        i=start
        if start>end:
            start,end=end,start
        if start==end:
            return True,0
        while i<=end:
            if 'exp' in function:
                inter=function.replace('exp','_')
                inter=inter.replace('x',str(i))
                rst=inter.replace('_','exp')
            else:
                rst=function.replace('x',str(i))
            total+=eval(rst)*dx
            i+=dx
        else:
            return True,round(total,2)
    except Exception as e:
        return False,e
