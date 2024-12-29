class dataSet:    
    def __init__(self,flag,data):
        '''
        Inputs flag(int) ,data(list)
        flag=0: [x,f]
        flag=1: [(start,end),f]
        flag=2: [x,x,x,...]
        '''
        self.data=data
        self.error=False
        self.e_msg=''
        self.is_valid=True
        self.flag=flag
        
        self.I=[]
        self.x=[]
        self.f=[]
        self.starts=[]
        self.ends=[]

        self.is_c_same=True
        self.c=None
        self.range=None

        self.has_same_limits_boundries=False
        self.limits=[]
        self.boundries_starts=[]
        self.boundries_ends=[]
        
        self.mode_classes=[]
        self.modes=[]
        self.mean=None
        self.median=None
        self.Q1=None
        self.Q3=None
        self.Q3_Q1=None

        self.outer_points=''

        self.MD=None
        self.Sk=None
        self.CV=None
        self.Var=None
        self.SD=None

        
        self.setData()

        self.count=sum(self.f)
        
        if sum(self.f)<=0:
            self.error=True
            self.e_msg='Empty Data'
        if self.flag==0:
            if not self.error:
                self.getRange()
            if not self.error:
                self.getMode()
            if not self.error:
                self.getQ1Q2Q3()
            if not self.error:
                self.getMean()
            if not self.error:
                self.getOuterPoints()
        else:
            if not self.error:
                self.getLimitsBoundries()
            if not self.error:
                self.getRange()
            if not self.error:
                self.getClassSize()
            if not self.error:
                self.getMidValue()
            if not self.error:
                self.getMode()
            if not self.error:
                self.getQ1Q2Q3()
            if not self.error:
                self.getMean()
            if not self.error:
                self.getOuterPoints()
        if not self.error:
                self.getMD()
        if not self.error:
                self.getVariation()
        if not self.error:
                self.getCV()
        if not self.error:
                self.getSk()
        if not self.error:
                self.rounder()
    def rounder(self):
        self.mean=round(self.mean,3)
        self.median=round(self.median,3)
        self.Q1=round(self.Q1,3)
        self.Q3=round(self.Q3,3)
        self.Q3_Q1=round(self.Q3_Q1,3)
        self.MD=round(self.MD,3)
        self.CV=round(self.CV,3)
        self.Var=round(self.Var,3)
        self.SD=round(self.SD,3)
    def setData(self):
        if self.flag==0:
            self.x=self.data[0]
            self.f=self.data[1]
        else:
            self.I=self.data[0]
            self.f=self.data[3]
            self.starts=self.data[1]
            self.ends=self.data[2]
    def getRange(self):
        if self.flag==1:
            self.range=(self.boundries_starts[0],self.boundries_ends[-1])
        else:
            self.range=(self.x[0],self.x[-1])
    def getClassSize(self):
        for i in range(len(self.boundries_starts)-1):
            if self.boundries_ends[i]-self.boundries_starts[i]!=self.boundries_ends[i+1]-self.boundries_starts[i+1]:
                self.is_c_same=False
                break
        else:
            self.is_c_same=True
        if self.is_c_same:
            self.c=self.boundries_ends[0]-self.boundries_starts[0]
        else:
            self.c=[]
            for i in range(len(self.boundries_starts)):
                self.c.append(self.boundries_ends[i]-self.boundries_starts[i])
    def getLimitsBoundries(self):
        #0-9 10-19 20-29 ...
        for i in range(len(self.starts)-1):
            if self.starts[i+1]-self.ends[i]!=0:
                self.has_same_limits_boundries=False
                break
        else:
            self.has_same_limits_boundries=True
        if self.has_same_limits_boundries:
            self.limits=self.I
            self.boundries_starts=self.starts
            self.boundries_ends=self.ends
        else:
            self.limits=self.I
            add=0.5#default
            for i in range(len(self.starts)-1):
                self.boundries_starts.append(self.starts[i]-add)
                add=(self.starts[i+1]-self.ends[i])/2
                self.boundries_ends.append(self.ends[i]+add)
            self.boundries_starts.append(self.starts[-1]-add)
            self.boundries_ends.append(self.ends[-1]+0.5)#0.5 is default
    def getMidValue(self):
        for i in range(len(self.boundries_starts)):
            self.x.append((self.boundries_starts[i]+self.boundries_ends[i])/2)

    def getMode(self):
        if self.flag==0:
            mode_f=max(self.f)
            i=0
            while self.f[i:].count(mode_f)>0:
                index=self.f[i:].index(mode_f)+i
                self.modes.append(self.x[index])
                i=index+1
        else:
            mode_f=max(self.f)
            i=0
            self.mode_classes=[]
            while self.f[i:].count(mode_f)>0:
                index=self.f[i:].index(mode_f)+i
                self.mode_classes.append(index)
                i=index+1
            for i in self.mode_classes:
                L1=self.boundries_starts[i]
                if i==0:
                    delta1=self.f[i]
                else:
                    delta1=self.f[i]-self.f[i-1]
                if i==len(self.boundries_starts)-1:
                    delta2=self.f[-1]
                else:
                    delta2=self.f[i]-self.f[i+1]
                if self.is_c_same:
                    self.modes.append(round(L1+(delta1/(delta1+delta2))*self.c,3))
                else:
                    self.modes.append(round(L1+(delta1/(delta1+delta2))*self.c[i],3))
    def getQ1Q2Q3(self):
        if self.flag==1:
            #start+f/gap(start end)*gap(start f)
            sum_f=sum(self.f)
            n1=(sum_f+1)/4
            n2=(sum_f+1)/2            
            n3=sum(self.f)/4*3
            n123=[n1,n2,n3]
            #sum frequencies
            ans=[]
            clz=None
            for n in n123:
                total=0
                OK=False
                for i in range(len(self.f)):
                    if total<n<(total+self.f[i]):
                        clz=i
                        break
                    elif total==n:
                        ans.append(self.boundries_starts[i])
                        OK=True
                        break
                    elif total+self.f[i]==n:
                        ans.append(self.boundries_ends[i])
                        OK=True
                        break
                    else:
                        total=total+self.f[i]
                else:
                    clz=None
                
                if clz!=None:
                    ans.append(self.boundries_starts[clz]+((self.boundries_ends[clz]-self.boundries_starts[clz])/self.f[clz]*(n-total)))
                elif not OK:
                    self.error=True
                    self.e_msg='Error in finding median'
                    return
                else:
                    pass
            self.Q1=ans[0]
            self.median=ans[1]
            self.Q3=ans[2]
        else:
            x_data=[]
            for i in range(len(self.x)):
                x_data.extend([self.x[i]]*self.f[i])
            sum_f=sum(self.f)
            n1=(sum_f)//4
            n2=(sum_f)//2            
            n3=(sum_f)*3//4
            self.Q1=x_data[n1]
            self.median=x_data[n2]
            self.Q3=x_data[n3]
        self.Q3_Q1=self.Q3-self.Q1
    def getMean(self):
        total_x=0
        total_f=0
        for i in range(len(self.x)):
            total_x=total_x+self.x[i]*self.f[i]
            total_f=total_f+self.f[i]
        self.mean=round(total_x/total_f,3)
    def getOuterPoints(self):
        out_start=round(self.Q1-(self.Q3-self.Q1)*3/2,3)
        out_end=round(self.Q3+(self.Q3-self.Q1)*3/2,3)
        self.outer_points=''
        if self.flag==0:
            for x in self.x:
                if x<out_start or x>out_end:
                    self.outer_points+=str(x)
            if self.outer_points=='':
                self.outer_points='No outer Points'
        else:
            if self.boundries_starts[0]<out_start:
                self.outer_points+=str(self.boundries_starts[0])+' <= data < '+str(out_start)+'\n'
            if self.boundries_ends[-1]>out_end:
                self.outer_points+=str(out_end)+' < data <= '+str(self.boundries_ends[-1])+'\n'
            if self.outer_points=='':
                self.outer_points='No outer Points'
    def getMD(self):
        '''
        MD=Mean Daviation
        '''
        total=0
        for i in range(len(self.f)):
            total+=self.f[i]*abs(self.x[i]-self.mean)
        self.MD=total/sum(self.f)
    def getVariation(self):
        total=0
        for i in range(len(self.f)):
            total+=self.f[i]*(self.x[i]-self.mean)**2
        self.Var=total/sum(self.f)
        self.SD=self.Var**0.5
    def getCV(self):
        try:
            self.CV=self.SD*100/self.mean
        except:
            self.CV='Infinity'
    def getSk(self):
        try:
            self.Sk=round(3*(self.mean-self.median)/self.SD,3)
            if self.Sk>0:
                self.Sk='Sk = '+str(self.Sk)+'\nPositive (Mode < Median < Mean)'
            elif self.Sk<0:
                self.Sk='Sk = '+str(self.Sk)+'\nNegative (Mode > Median > Mean)'
            else:
                self.Sk='Sk = '+str(self.Sk)+'\n (Mode = Median = Mean)'
        except:
            if self.mean-self.median>0:
                self.Sk='Sk -> Infinity\nPositive (Mode < Median < Mean)'
            elif self.mean-self.median<0:
                self.Sk='Sk -> Infinity\nNegative (Mode > Median > Mean)'
            else:
                self.Sk='Sk -> Infinity\n (Mode = Median = Mean)'
    def getData(self):
        if not self.error:
            show=''
            if self.flag==1:
                show+='Start\t\t\t\tEnd\t\t\t\tx\t\t\t\tf\n'
                for i in range(len(self.x)):
                    show+=str(self.boundries_starts[i])+'\t\t\t\t'+str(self.boundries_ends[i])+'\t\t\t\t'+str(self.x[i])+'\t\t\t\t'+str(self.f[i])+'\n'
                show+='\n\n'

                if self.is_c_same:
                    show+='C = '+str(self.c)
                else:
                    show+='C = '
                    for c in self.c:
                        show+=str(c)+' , '
                show+='\n\n'

                show+='Limits = \n'
                for i in range(len(self.x)):
                    show+=str(self.limits[i][0])+'\t'+str(self.limits[i][1])+'\n'
                show+='\n\n'

            else:
                show+='x\t\t\t\tf\n'
                for i in range(len(self.x)):
                    show+=str(self.x[i])+'\t\t\t\t'+str(self.f[i])+'\n'
                show+='\n\n'

            show+='Range = '+str(self.range[0])+' to '+str(self.range[1])
            show+='\n\n'

            show+='Mode = '
            for i in range(len(self.modes)):
                show+=str(self.modes[i])+' , '
            show+='\n\n'

            show+='Mean = '+str(self.mean)
            show+='\n\n'

            show+='Median = '+str(self.median)
            show+='\n\n'

            show+='Q1 = '+str(self.Q1)
            show+='\n\n'

            show+='Q3 = '+str(self.Q3)
            show+='\n\n'

            show+='Q3 - Q1 = '+str(self.Q3_Q1)
            show+='\n\n'

            show+='Outer Points = '+str(self.outer_points)
            show+='\n\n'

            show+='MD = '+str(self.MD)
            show+='\n\n'

            show+=str(self.Sk)
            show+='\n\n'

            show+='CV = '+str(self.CV)
            show+='\n\n'

            show+='Var = '+str(self.Var)
            show+='\n\n'

            show+='SD = '+str(self.SD)
            show+='\n\n'

            show+='Count = '+str(self.count)
            show+='\n\n'

            return True,show


            #self.c, self.range,self.limits,self.boundries_starts,self.boundries_ends,self.modes, self.mean,self.median,self.Q1=None
            #self.Q3,self.Q3_Q1,self.outer_points,self.MD,self.Sk,self.CV,self.Var,self.SD,self.count
        else:
            return False,self.e_msg
def characterSyntaxValidation(flag,data):
    if flag==0:
        #x1 f1
        #x2 f2
        #...
        try:
            data=data.split('\n')
            while '' in data:
                data.remove('')
            if len(data)<1:
                return False,'Empty data'
            for i in range(len(data)):
                data[i]=data[i].split()
                while '' in data[i]:
                    data[i].remove('')
                if len(data[i])!=2:
                    return False,'Invalid Syntax. Use x(space)f'
                data[i][0]=float(data[i][0])
                data[i][1]=int(data[i][1])
            return True,data
        except:
            return False,'Invalid data'
    elif flag==1:
        #x1.1 x1.2 f
        #x2.1 x2.2 f
        #...
        try:
            data=data.split('\n')
            while '' in data:
                data.remove('')
            if len(data)<1:
                return False,'Empty data'
            for i in range(len(data)):
                data[i]=data[i].split()
                while '' in data[i]:
                    data[i].remove('')
                if len(data[i])!=3:
                    return False,'Invalid Syntax. Use start(space)end(space)f'
                data[i][0]=float(data[i][0])
                data[i][1]=float(data[i][1])
                data[i][2]=int(data[i][2])
            return True,data
        except:
            return False,'Invalid data'
    elif flag==2:
        #x1 x2 x3 ....
        try:
            data=data.replace('\n',' ')
            data=data.split()
            while '' in data:
                data.remove('')
            if len(data)<1:
                return False,'number of data should be greater than 1'
            for i in range(len(data)):
                data[i]=float(data[i])
            return True,data
        except:
            return False,'Invalid data'
    else:
        return False,'Invalid Flag'
def dataValidation(flag,data):
    if flag==0:
        #data=[[x,f],[x,f],...]
        x=[]
        f=[]
        for i in range(len(data)):
            x.append(data[i][0])
            f.append(data[i][1])
        for ele in f:
            if ele<=0:
                return False,'Frequency should be positive'
        while len(x)!=len(set(x)):
            for i in range(len(x)):
                total=f[i]
                if x.count(x[i])!=1:
                    s,index=search(x,x[i])
                    index.remove(i)
                    for k in index:
                        x.pop(k)
                        total=total+f[k]
                        f[k]='x'
                    f[i]=total
                    while 'x' in f:
                        f.remove('x')
                    else:
                        break
        x,f=insertionSort(x,f)
        return True,[x,f]
    elif flag==2:
        #[x, x, x, x,....]
        data.sort()
        x=[]
        f=[]
        count=0
        for i in range(len(data)-1):
            count+=1
            if data[i]!=data[i+1]:
                x.append(data[i])
                f.append(count)
                count=0
        if count!=0:
            x.append(data[-1])
            f.append(count+1)
        data=[x,f]
        return True,data
    else:
        #start end frequency
        I=[]
        f=[]
        starts=[]
        ends=[]
        for i in range(len(data)):
            starts.append(data[i][0])
            ends.append(data[i][1])
            f.append(data[i][2])
        for ele in f:
            if ele<0:
                return False,'Frequency should be positive'
        #if len(starts)!=len(ends):
        #   return False,'Starting points count does not match with Ending points count'
        while len(starts)!=len(set(starts)) and len(ends)!=len(set(ends)):
            for i in range(len(starts)):
                total=f[i]
                if starts.count(starts[i])!=1 and ends.count(ends[i])!=1:
                    s,index_start=search(starts,starts[i])
                    s,index_end=search(ends,ends[i])
                    if index_start!=index_end:
                        return False,'Invalid Intervals (Same start has different ends)'
                    index_start.remove(i)
                    index_end.remove(i)
                    for k in index_start:
                        starts.pop(k)
                        ends.pop(k)
                        total=total+f[k]
                        f[k]='x'
                    f[i]=total
                    while 'x' in f:
                        f.remove('x')
                    else:
                        break
        starts,ends,f=insertionSort3(starts,ends,f)
        for i in range(len(starts)):
            if starts[i]>=ends[i]:
                return False,'Invalid Intervals (start is greater than the end)'
            if i!=0:
                if ends[i-1]>starts[i]:
                    return False,'Invalid Intervals (Next interval starts before ending previous one)'
        for i in range(len(starts)):
            I.append((starts[i],ends[i]))
        return True,[I,starts,ends,f]
def insertionSort(data1,data2):
    #len(data1)=len(data2)
    for i in range(len(data1)):
        l=min(data1[i:])
        l_index=data1.index(l)
        data1[i],data1[l_index]=l,data1[i]
        data2[i],data2[l_index]=data2[l_index],data2[i]
    return data1,data2
def insertionSort3(data1,data2,data3):
    #len(data1)=len(data2)=len(data3)
    for i in range(len(data1)):
        l=min(data1[i:])
        l_index=data1.index(l)
        data1[i],data1[l_index]=l,data1[i]
        data2[i],data2[l_index]=data2[l_index],data2[i]
        data3[i],data3[l_index]=data3[l_index],data3[i]
    return data1,data2,data3
def search(data,x):
    '''
    Input data(list),x(value)
    output index_list(list)
    '''
    index=[]
    for i in range(len(data)):
        if data[i]==x:
            index.append(i)
    if len(index)==0:
        return False,[]
    else:
        return True,index
def saveData(flag,data):
    state,data=characterSyntaxValidation(flag,data)
    if not state:
        return state,data
    state,data=dataValidation(flag,data)
    if not state:
        return state,data
    if flag==2:
        flag=0
    data=dataSet(flag,data)
    if data.error:
        return False,data.e_msg
    else:
        return True,data
def percentage(data,i):
    '''
    Inputs : data ,i(string)
    Output: boolean, msg/value
    '''
    try:
        i=float(i)
        if i<0 or i>100:
            return False,'i should be less than or equal 100'
    except:
        return False,'i should be Positive Integer'
    return findN(data,str(i*(sum(data.f))/100))
def unitTransformation(data,ab):
    '''
    y=ax+b
    Inputs : data ,a b (str)
    Output : boolean,mean y,SD y
    '''
    try:
        l=ab.split()
        while '' in l:
            l.remove('')
        if len(l)!=2:
            return False,'Need a and b'
        a=l[0]
        b=l[1]
        a=float(a)
        b=float(b)
    except:
        return False,'Invalid a or b'
    return True,round(a*data.mean+b,3),round(abs(a)*data.SD,3)
def combine(data1,data2):
    mean=(data1.count*data1.mean+data2.count*data2.mean)/(data1.count+data2.count)
    SD=(data1.count*(data1.Var+(data1.mean-mean)**2)+data2.count*(data2.Var+(data2.mean-mean)**2))/(data1.count+data2.count)
    return True,round(mean,3),round(SD,3)
def getZScore(data,x):
    '''
    Inputs : data,x
    Outputs: boolean,Zscore
    '''
    try:
        x=float(x)
        return True,round((x-data.mean)/data.SD,3)
    except:
        return True,'Infinity'
def findN(data,n):
    try:
        if data.flag==1:
            n=float(n)
        else:
            n=int(round(float(n)))
    except:
        return False,'n should be Positive Integer'
    if n<0:
        return False,'n should be Positive Integer'
    if n>sum(data.f):
        return False,'n should be less than or equal 100'
    if data.flag==1:
        #start+f/gap(start end)*gap(start f)
        sum_f=sum(data.f)
        ans=None
        total=0
        clz=None
        OK=False
        for i in range(len(data.f)):
            if total<n<(total+data.f[i]):
                clz=i
                break
            elif total==n:
                ans=(data.boundries_starts[i])
                OK=True
                break
            elif total+data.f[i]==n:
                ans=(data.boundries_ends[i])
                OK=True
                break
            else:
                total=total+data.f[i]
        else:
            clz=None
        if clz!=None:
            ans=(data.boundries_starts[clz]+((data.boundries_ends[clz]-data.boundries_starts[clz])/data.f[clz]*(n-total)))
        elif not OK:
            data.error=True
            data.e_msg='Error in finding N'
            return False,data.e_msg
        else:
            pass
        return True,str(round(ans,3))
    else:
        x_data=[]
        for i in range(len(data.x)):
            x_data.extend([data.x[i]]*data.f[i])
        return True,round(x_data[n-1],3)
def divert(flag,*data):
    if flag==0:
        return combine(data[0],data[1])
    elif flag==1:
        return unitTransformation(data[0],data[1])
    elif flag==2:
        return percentage(data[0],data[1])
    elif flag==3:
        return getZScore(data[0],data[1])
    else:
        return False,'Select a Type'
