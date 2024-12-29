from math import atan
class straightLines:
    '''only for 2D'''
    def __init__(self,flag,data):
        self.point1=None
        self.point2=None
        self.gradient=None
        self.y_intercept=None
        self.error=None
        self.e_msg=None
        self.line=None#ax+by+c=0-->[a,b,c]
        self.show=None
        if flag==0:
            #2points
            if data[0]==data[1]:
                self.error,self.e_msg=True,'Input 2 different points'
            else:
                self.point1=data[0]
                self.point2=data[1]
                self.gradient=getGradient(self.point1,self.point2)
                if self.gradient!='infi':
                    self.y_intercept=round(-self.gradient*self.point1[0]+self.point1[1],3)
                    self.line=[self.gradient,-1,self.y_intercept]
                else:
                    #X=k
                    self.y_intercept='No'
                    self.line=[1,0,-self.point1[0]]
        elif flag==1:
            #point,gradient
            self.point1=data[0]
            self.gradient=data[1]
            if self.gradient!='infi':
                self.y_intercept=round(-self.gradient*self.point1[0]+self.point1[1],3)
                self.line=[self.gradient,-1,self.y_intercept]
            else:
                #X=k
                self.y_intercept='No'
                self.line=[1,0,self.point1[0]]
        elif flag==2:
            #gradient,y_intercept
            self.gradient=data[0]
            if self.gradient!='infi':
                self.y_intercept=data[1]
                self.line=[self.gradient,-1,self.y_intercept]
            else:
                #X=k
                self.error,self.e_msg=True,'x=k type equations need a point'
        elif flag==3:
            self.line=data
            dy=-self.line[0]
            dx=self.line[1]
            if dx==0:
                self.gradient='infi'
                self.y_intercept='No'
            else:
                self.gradient=round(dy/dx,3)
                self.y_intercept=-self.line[2]/dx

        else:
            self.error,self.e_msg=True,'Select the method of inputs'
        if self.line!=None:
            self.show='y = (m)*x + (c)'
            if self.gradient!='infi':
                self.show=self.show.replace('m',str(self.gradient))
                self.show=self.show.replace('c',str(self.y_intercept))
            elif self.gradient==0:
                self.show='y = (k)'
                self.show=self.show.replace('k',str(round(-self.line[2]/self.line[1],3)))
            else:
                self.show='x = (k)'
                self.show=self.show.replace('k',str(round(-self.line[2]/self.line[0],3)))
    def getShow(self):
        return self.show
    def getDetails(self):
        return self.gradient,self.y_intercept,self.line,self.show
def getGradient(p1,p2):
    dy=p2[1]-p1[1]
    dx=p2[0]-p1[0]
    if dx==0:
        return 'infi'
    else:
        return round(dy/dx,3)
def saveLine(data,flag):
    '''
    Inputs  data(String),flag(int)
    Outputs  boolean,(line or error msg)
    '''
    state,data=checkData(data,flag)
    if state[0]:
        output=straightLines(flag,data)
        return True,output
    else:
        return state
def checkData(data,flag):
    is_valid=True
    msg=''
    if flag==0:
        return convertStringToPoints(data)
    elif flag==1:
        data=data.split()
        while '' in data:
            data.remove('')
        if len(data)!=2:
            is_valid=False
            msg='Insert only a point and the gradient using correct syntax'
            return (is_valid,msg),data
        #check point
        (is_valid,msg),data[0]=isValidPoint(data[0])
        if is_valid==False:
            return (is_valid,msg),data
        #check gradient
        try:
            data[1]=float(data[1])
        except:
            is_valid=False
            msg='Invalid gradient'
            return (is_valid,msg),data

    elif flag==2:
        data=data.split()
        while '' in data:
            data.remove('')
        if len(data)!=2:
            is_valid=False
            msg='Insert only the gradient and the y intercept using correct syntax'
            return (is_valid,msg),data
        #check gradient,intercept
        for p in range(2):
            try:
                data[p]=float(data[p])
            except:
                is_valid=False
                msg='Invalid gradient or y intercept'
                return (is_valid,msg),data
    else:
        #flag==3
        data=data.split()
        while '' in data:
            data.remove('')
        if len(data)!=3:
            is_valid=False
            msg='Insert the 3 line coefficients using correct syntax'
            return (is_valid,msg),data
        #check coefficients
        for p in range(3):
            try:
                data[p]=float(data[p])
            except:
                is_valid=False
                msg='Invalid coefficients'
                return (is_valid,msg),data
    return (is_valid,msg),data

def getLength(p1,p2):
    return True,round(((p1[0]-p2[0])**2+(p1[1]-p2[1])**2)**0.5,3)
def isParallel(l1,l2):
    if l1.gradient==l2.gradient:
        return True,'Parallel'
    else:
        return False,'Not Parallel'
def isShunt(l1,l2):
    m1,m2=l1.gradient,l2.gradient
    if m1!='infi' and m2!='infi':
        if l1.gradient*l2.gradient==-1:
            return True,'Perpendicular'
        else:
            return False,'Not Perpendicular'
    else:
        if (m1=='infi' or m2=='infi') and (m1==0 or m2==0):
            return True,'Perpendicular'
        else:
            return False,'Not Perpendicular'

def getAngle(l1,l2):
    m1=l1.gradient
    m2=l2.gradient
    if m1=='infi':
        t1=11/7
    else:
        t1=atan(m1)
        if t1<0:
            t1=22/7+t1
    if m2=='infi':
        t2=11/7
    else:
        t2=atan(m2)
        if t2<0:
            t2=22/7+t2
    angle=abs((t1-t2)/22*7*180)
    if angle>90:
        angle=180-angle
    return round(angle,3)
def isAvailableOnLine(l,p):
    '''
    Inputs line,point(string)
    Outputs boolean,msg
    '''
    state,p=isValidPoint(p)
    if state[0]:
        if 0==round(l.line[0]*p[0]+l.line[1]*p[1]+l.line[2],3):
            return True,'On the line'
        else:
            return False,'Not on line'
    else:
        return state
def getDistanceToPoint(l,p):
    state,p=isValidPoint(p)
    if state[0]:
        a,b,c=l.line[0],l.line[1],l.line[2]
        return True,abs(round((a*p[0]+b*p[1]+c)/(a**2+b**2)**0.5,3))
    else:
       return state
def angleBisectors():
    pass
def isSameSide(l,data):
    state,data=convertStringToPoints(data)
    if state[0]:
        p1=data[0]
        p2=data[1]
        a,b,c=l.line[0],l.line[1],l.line[2]
        rst=(a*p1[0]+b*p1[1]+c)*(a*p2[0]+b*p2[1]+c)
        if rst<0:
            return True,'Both side'
        elif rst>0:
            return True,'Same side'
        else:
            return True,'at least one of them is on the line'
    else:
        return False,data
def distanceBetweenPoints(data):
    state,data=convertStringToPoints(data)
    if state:
        return getLength(data[0],data[1])
    else:
        return state,data
def distanceBetweenLines(l1,l2):
    if isParallel(l1,l2)[0]:
        return abs(round((l1.line[2]-l2.line[2])/(l1.line[0]**2+l1.line[1]**2)**0.5,3))
    else:
        return 0
def getIntersectionPoint(l1,l2):
    my_matrix=str(l1.line[0])+' '+str(l1.line[1])+' '+str(-l1.line[2])+'\n'+str(l2.line[0])+' '+str(l2.line[1])+' '+str(-l2.line[2])
    return True,my_matrix
    #use simmultaneous equations
def ratioMethod(m,n,points):
    points=points.split('x')
    if m=='' or n=='':
        return False,'Empty m or n'
    else:
        m,n=int(m),int(n)
        if m==0 or n==0:
            return False,"m or can not be zero"
    count=0
    for i in range(3):
        if points[i]!='None':
            state,points[i]=isValidPoint(points[i])
            if state[0]:
                count+=1
            else:
                return False,'Invalid points'
    if count==2:
        p1=points[0]
        p2=points[1]
        p3=points[2]
        if p1=='None':
            if p2==p3:
                return False,'Need 2 different points'
            return outerRatio(p3,p2,m+n,m)
        elif p3=='None':
            if p2==p1:
                return False,'Need 2 different points'
            return outerRatio(p1,p2,m+n,n)
        else:
            if p1==p3:
                return False,'Need 2 different points'
            return innerRatio(p1,p3,m,n)
    else:
        return False,'Need 2 points and 2 ratios'
def outerRatio(p1,p2,m,n):
    if m-n<=0:
        return False,'m and n should be different'
    x=round((m*p2[0]-n*p1[0])/(m-n),3)
    y=round((m*p2[1]-n*p1[1])/(m-n),3)
    return True,(x,y)
def innerRatio(p1,p2,m,n):
    x=round((m*p2[0]+n*p1[0])/(m+n),3)
    y=round((m*p2[1]+n*p1[1])/(m+n),3)
    return True,(x,y)
def lineThroughIntercept(l1,l2,point):
    #p=getIntersectionPoint(l1,l2)
    #saveLine(0,p,point)
    pass

############################################################################
                                  #Circle
############################################################################

class circle:
    def __init__(self,flag,data):
        self.center=None
        self.radius=None
        self.g=None
        self.f=None
        self.c=None
        self.error=None
        self.e_msg=None
        self.crcl=None#x^2+y^2+2gx+2fy+c=0-->[g,f,c]
        self.show1,self.show2=None,None
        if flag==0:
            #center radius
            self.center=data[0]
            self.radius=data[1]
            self.g=-self.center[0]
            self.f=-self.center[1]
            self.c=(self.center[0]**2+self.center[1]**2-self.radius**2)
            self.crcl=[self.g,self.f,self.c]
            self.show1='x^2 + y^2 + '+str(round(2*self.g,3))+'*x + '+str(round(2*self.f,3))+'*y + '+str(round(self.c,3))+' = 0'
            self.show2='(x - '+str(round(self.center[0],3))+')^2 + (y - '+str(round(self.center[1],3))+')^2 = '+str(round(self.radius**2,3))
            self.show1=self.show1.replace('+ -','- ')
            self.show2=self.show2.replace('- -','+ ')
        else:
            #flag = 1
            #g f c
            self.g=data[0]
            self.f=data[1]
            self.c=data[2]
            self.center=(-self.g,-self.f)
            r_2=self.center[0]**2+self.center[1]**2-self.c
            self.crcl=[self.g,self.f,self.c]
            if r_2>0:
                self.radius=round(r_2**0.5,3)
                self.show1='x^2 + y^2 + '+str(round(2*self.g,3))+'*x + '+str(round(2*self.f,3))+'*y + '+str(round(self.c,3))+' = 0'
                self.show2='(x - '+str(round(self.center[0],3))+')^2 + (y - '+str(round(self.center[1],3))+')^2 = '+str(round(self.radius**2,3))
                self.show1=self.show1.replace('+ -','- ')
                self.show2=self.show2.replace('- -','+ ')
            elif r_2==0:
                self.error,self.e_msg=True,'Circle is a point'
            else:
                self.error,self.e_msg=True,'Circle is Imaginary'


    def getShow(self):
        return self.show1,self.show2
    def getDetails(self):
        return self.center,self.radius,self.crcl,self.show1,self.show2
def saveCircle(data,flag):
    state,data=checkDataCircle(data,flag)
    if state[0]:
        if flag==2:
            flag=1
            data=circleByDiamterPoints(data)
        output=circle(flag,data)
        return True,output
    else:
        return state
def checkDataCircle(data,flag):
    is_valid=True
    msg=''
    if flag==1:
        data=data.split()
        while '' in data:
            data.remove('')
        if len(data)!=3:
            is_valid=False
            msg='Insert only 3 coffiecients (g,f,c) using correct syntax'
            return (is_valid,msg),data
        for p in range(3):
            try:
                data[p]=float(data[p])
            except:
                is_valid=False
                msg='Invalid values'
                return (is_valid,msg),data
    elif flag==0:
        data=data.split()
        if len(data)!=2:
            is_valid=False,
            msg='Need only one point and the radius'
            return (is_valid,msg),data
        state,data[0]=isValidPoint(data[0])
        if not state[0]:
            return state,data
        #check radius
        try:
            data[1]=float(data[1])
        except:
            is_valid=False
            msg='Invalid radius'
            return (is_valid,msg),data
        if data[1]<0:
            is_valid=False
            msg='Circle is Imaginary'
            return (is_valid,msg),data
        if data[1]==0:
            is_valid=False
            msg='Circle is a point'
            return (is_valid,msg),data
    elif flag==2:
        state,data=convertStringToPoints(data)
        if not state[0]:
            return state,data
    else:
        is_valid=False
        msg='Select a Type'
        return (is_valid,msg),data
    return (is_valid,msg),data
def circleByDiamterPoints(data):
    p1=data[0]
    p2=data[1]
    g=-p1[0]-p2[0]
    f=-p1[1]-p2[1]
    c=p1[1]*p2[1]+p1[0]*p2[0]
    data=[g,f,c]
    return data
def circleCalculations(flag,*data):
    if flag==0:
        c=data[0]
        p=data[1]
        return positionOfPoint(c,p)
    elif flag==1:
        c=data[0]
        l=data[1]
        return positionOfCircleAndLine(c,l)
    elif flag==2:
        c=data[0]
        p=data[1]
        state,data=positionOfPoint(c,p)
        if state and data=='Point is On the Circle':
            return tangentByPointOntheCircle(c,p)
        else:
            return False,'Point is not On the Circle'
    elif flag==3:
        c=data[0]
        p=data[1]
        state,data=positionOfPoint(c,p)
        if state and data!='Point is Inside the Circle':
            return tangentByPointOutSidetheCircle(c,p)
        else:
            return False,'Point is Inside the Circle'
    elif flag==4:
        c=data[0]
        p=data[1]
        return lengthOfTangent(c,p)
    elif flag==5:
        c=data[0]
        p=data[1]
        state,data=positionOfPoint(c,p)
        if state and data=='Point is Outside the Circle':
            return tangentChord(c,p)
        else:
            return False,'Point is not OutSide the Circle'
    elif flag==6:
        c=data[0]
        l=data[1]
        p=data[2]
        return circleThroughCircleAndLine(c,l,p)
    elif flag==7:
        c1=data[0]
        c2=data[1]
        return positionOf2Circles(c1,c2)
    elif flag==8:
        c1=data[0]
        c2=data[1]
        p=data[2]
        return circleThrough2Circles(c1,c2,p)
    elif flag==9:
        c1=data[0]
        c2=data[1]
        return isInitiallyIntercept(c1,c2)
    elif flag==10:
        c1=data[0]
        c2=data[1]
        return isBiSecting(c1,c2)
    else:
        return False,'Give a valid flag'
def positionOfPoint(c,p):
    '''Inputs circle,position(string)
    outputs boolean,msg'''
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        ans=round(x**2+y**2+2*c.g*x+2*c.f*y+c.c,3)
        if ans==0:
            return True,'Point is On the Circle'
        elif ans>0:
            return True,'Point is Outside the Circle'
        else:
            return True,'Point is Inside the Circle'
    else:
        return False,state[1]
def positionOfCircleAndLine(c,l):
    r=c.radius
    s,d=getDistanceToPoint(l,str(c.center[0])+','+str(c.center[1]))
    if s:
        if r==d:
            return True,'The Circle and the Line are Touching'
        elif r>d:
            return True,'The Circle and the Line are Intercepting'
        else:
            return True,'The Circle and the Line are neither Intercepting nor Touching'
    else:
        return s,d
def tangentByPointOntheCircle(c,p):
    '''(x1+g)*x+(y1+f)*y+(c+gx1+fy1)=0'''
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        ans=str(round(x+c.g,3))+' '+str(round(y+c.f,3))+' '+str(round(c.g*x+c.f*y+c.c,3))
        state1,l=saveLine(ans,3)
        if not state1:
            return False,l
        elif l.error:
            return False,l.e_msg
        else:
            return True,l.show
    else:
        return False,state[1]
def tangentByPointOutSidetheCircle(c,p):
    '''m^2 ((g+x)^2-r^2)  + m  (-2*(g+x)*(f+y))  +   ((f+y)^2-r^2) = 0'''
    P_copy=p[:]#For future uses
    C=c
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        a=str((c.g+x)**2-c.radius**2)
        b=str(-2*(c.g+x)*(c.f+y))
        c=str((c.f+y)**2-c.radius**2)
        state,data,flag=quadraticEquations(a,b,c)
        if state:
            if flag==1:
                m=str(data[0])
                p=str(x)+','+str(y)
                data=p+' '+m
                s,l=saveLine(data,1)
                if s:
                    if l.error:
                        return False,l.e_msg
                    else:
                        return True,'Point is On the Circle'+'\n'+l.show
                else:
                    return s,l
            elif flag==2:
                m1=str(data[0])
                m2=str(data[1])
                m=[m1,m2]
                lines=[]
                for i in range(2):
                    p=str(x)+','+str(y)
                    data=p+' '+m[i]
                    state1,l=saveLine(data,1)
                    if not state1:
                        return False,l
                    elif l.error:
                        return False,l.e_msg
                    else:
                        lines.append(l)
                return True,lines[0].show+'\n'+lines[1].show
            elif flag==4:
                m1=str(data[0])
                virtual_points1='1,'+str(y)+' 2,'+str(y)
                m2=data[1]#infi
                virtual_points2=str(x)+',1 '+str(x)+',2'
                lines=[]
                points=[virtual_points1,virtual_points2]
                for i in range(2):
                    state1,l=saveLine(points[i],0)
                    if not state1:
                        return False,l
                    elif l.error:
                        return False,l.e_msg
                    else:
                        lines.append(l)
                return True,lines[0].show+'\n'+lines[1].show
            else:
                #Point is On the Circle
                output=tangentByPointOntheCircle(C,P_copy)
                if output[0]:
                    output=(True,'Point is On the Circle'+'\n'+output[1])
                return output
        return False,data
    else:
        return False,state[1]
def lengthOfTangent(c,p):
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        ans=(x**2+y**2+2*c.g*x+2*c.f*y+c.c)**0.5
        if complex(ans).imag!=0:
            return False,"Point is inside the circle"
        return True,ans
    else:
        return False,state[1]
def tangentChord(c,p):#Jyaya=Chord
    return tangentByPointOntheCircle(c,p)
def circleThroughCircleAndLine(c,l,p):
    '''S/u=-lamda
    x^2+y^2+(2g+lamda*a)x+(2f+lamda*b)y+(c+lamda*c_line)=0
    '''
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        up=x**2+y**2+2*c.g*x+2*c.f*y+c.c
        down=l.line[0]*x+l.line[1]*y+l.line[2]
        if down==0:
            return False,'No Circle'
        else:
            lamda=round(up/down,3)
            g=c.g+lamda*l.line[0]
            f=c.f+lamda*l.line[1]
            c=c.c+lamda*l.line[2]
            data=str(g)+' '+str(f)+' '+str(c)#flag =3
            state1,c=saveCircle(data,1)
            if state1:
                return True,c.show1+'\n'+c.show2
            else:
                return False,'can not solve'
    else:
        return False,state[1]
def positionOf2Circles(c1,c2):
    '''
    Inputs circl1 and circle2
    Outputs boolean,msg(string)
    '''
    if c1==c2:
        return True,'Circles are Coincidal'
    state,AB=getLength(c1.center,c2.center)
    if c1.radius-c2.radius>0:
        msg='The Circle 2 is inside the Circle 1 and '
    else:
        msg='The Circle 1 is inside the Circle 2 and '
    if abs(c1.radius-c2.radius)==AB:
        return True,msg+'Circles are Touching Inside'
    elif abs(c1.radius-c2.radius)>AB:
        return True,msg+'Circles are neither Intercepting nor Touching Inside'
    elif abs(c1.radius+c2.radius)==AB:
        return True,'Circles are Touching Outside'
    elif abs(c1.radius+c2.radius)<AB:
        return True,'Circles are neither Intercepting nor Touching Outside'
    else:
        return True,'Circles are Intercepting'
def circleThrough2Circles(c1,c2,p):
    '''
    Inputs circle1,circle2,point(string)
    Outputs boolean,circle show(string)
    Concept->  s1+lamda*s2=0
                find lamda for given point
                create circle
    '''
    if c1==c2:
        return False,'Insert 2 different circles'
    state,AB=getLength(c1.center,c2.center)
    if not(abs(c1.radius-c2.radius)<=AB or abs(c1.radius+c2.radius)>=AB):
        return True,'These circles are not Intercepting'
    state,p=isValidPoint(p)
    if state[0]:
        x=p[0]
        y=p[1]
        up=x**2+y**2+2*c1.g*x+2*c1.f*y+c1.c
        down=x**2+y**2+2*c2.g*x+2*c2.f*y+c2.c
        if down==0:
            return False,'No circles for given data'
        else:
            if abs(c1.radius-c2.radius)==AB or abs(c1.radius+c2.radius)==AB:
                #lamda=-1
                co_effi=[2*c1.g-2*c2.g,2*c1.f-2*c2.f,c1.c-c2.c]
                data=str(co_effi[0])+' '+str(co_effi[1])+' '+str(co_effi[2])
                state1,l=saveLine(data,3)
                if not state1:
                    return False,l
                elif l.error:
                    return False,l.e_msg
                else:
                    if isAvailableOnLine(l,str(p[0])+','+str(p[1]))[0]:
                        return True,l.show+'\n given point is on the line'
                    else:
                        return True,l.show+'\n given point is not on the line'
            else:
                lamda=round(-up/down,3)
                co_effi=[1+lamda,1+lamda,2*c1.g+2*c2.g*lamda,2*c1.f+2*c2.f*lamda,c1.c+lamda*c2.c]
                if round(co_effi[0],3)!=0:
                    g=round(co_effi[2]/co_effi[0]/2,1)
                    f=round(co_effi[3]/co_effi[0]/2,1)
                    c=round(co_effi[4]/co_effi[0],1)
                    data=str(g)+' '+str(f)+' '+str(c)
                    state1,circle=saveCircle(data,1)
                    if not state1:
                        return False,circle
                    elif circle.error:
                        return False,circle.e_msg
                    else:
                        return True,circle.getShow()
                else:
                    return True,'No circles'
    else:
        return False,state[1]
def isInitiallyIntercept(c1,c2):
    if c1==c2:
        return True,'Insert 2 different Circles (Same circle is not Initially Intercepting with it)'
    if c1.c+c2.c==2*(c1.g*c2.g+c1.f*c2.f):
        return True,'Initially Intercept'
    else:
        return True,'Do not Initially Intercept'
def isBiSecting(c1,c2):
    if c1==c2:
        return True,'Insert 2 different Circles (Same circle is not bisecting with it)'
    r1=c1.radius
    r2=c2.radius
    r=min(r1,r2)
    if c1.g==c2.g and c1.f==c2.f and 2*r==c1.c-c2.c:
        return True,'Circles are BiSecting'
    else:
        return True,'Circles are not BiSecting'

#############################################################################
def isValidPoint(data):
    is_valid=True
    msg=''
    data=data.split(',')
    while '' in data:
        data.remove('')
    if len(data)!=2:
        is_valid=False
        msg='Insert point using correct syntax'
        return (is_valid,msg),data
    for i in range(2):
        try:
            data[i]=float(data[i])
        except:
            is_valid=False
            msg='Invalid point'
            return (is_valid,msg),data
    return (is_valid,msg),data
def convertStringToPoints(data):
    is_valid=True
    msg=''
    data=data.split()
    while '' in data:
        data.remove('')
    if len(data)!=2:
        is_valid=False
        msg='Insert only 2 points using correct syntax'
        return (is_valid,msg),data
    for p in range(2):
        data[p]=data[p].split(',')
        while '' in data[p]:
            data[p].remove('')
        if len(data[p])!=2:
            is_valid=False
            msg='Insert each point using correct syntax'
            return (is_valid,msg),data
        for i in range(2):
            try:
                data[p][i]=float(data[p][i])
            except:
                is_valid=False
                msg='Invalid points'
                return (is_valid,msg),data
    return (is_valid,msg),data
def quadraticEquations(a,b,c):
    '''
    a,b,c are String
    '''
    if not(isNumber(a) and isNumber(b) and isNumber(c)):
        return False,'Invalid inputs',0
    a=float(a)
    b=float(b)
    c=float(c)
    if a==0:
        if b==0:
            return True,[],5#Point is On the Circle
        else:
            x1=0
            x2='infi'
            return True,[x1,x2],4#horizotal vertical
    x1=(-b+(b**2-4*a*c)**0.5)/(2*a)
    x2=(-b-(b**2-4*a*c)**0.5)/(2*a)
    if x1==x2:
        x1=round(x1,2)
        x2=round(x2,2)
        return True,[x1,x2],1#Point is On the Circle
    elif x1.imag==0:
        x1=round(x1,2)
        x2=round(x2,2)
        return True,[x1,x2],2#Normal
    else:
        x1=str(round(x1.real,2))+'+'+str(round(x1.imag,2))+'j'
        x1=complex(x1)
        x2=str(round(x2.real,2))+str(round(x2.imag,2))+'j'
        x2=complex(x2)
        alfa=x1+x2
        beta=x1*x2
        if alfa.imag==0:
            alfa=alfa.real
        if beta.imag==0:
            beta=beta.real
        return False,[x1,x2],3
def isNumber(n):
    '''n is String'''
    try:
        eval(n)
    except:
        return False
    else:
        return True
