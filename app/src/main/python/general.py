def isInteger(string_value):
    try:
        int(string_value)
    except:
        return False
    else:
        return True
def fact(n):
    if n>1:
        ans=1
        for i in range(2,n+1):
            ans*=i
        return ans
    elif n==1 or n==0:
        return 1
    else:
        return -1
def ncr(n,r):
    is_int_n=isInteger(n)
    is_int_r=isInteger(r)
    if is_int_n and is_int_r:
        n_fac=fact(int(n))
        r_fac=fact(int(r))
        n_r_fac=fact(int(n)-int(r))
        if n_fac!=-1 and r_fac!=-1 and n_r_fac!=-1:
            return (n_fac)/(n_r_fac*r_fac)
        else:
            return -1
    else:
        return -1
def npr(n,r):
    is_int_n=isInteger(n)
    is_int_r=isInteger(r)
    if is_int_n and is_int_r:
        n_fac=fact(int(n))
        r_fac=fact(int(r))
        if n_fac!=-1 and r_fac!=-1 :
            return (n_fac)/(r_fac)
        else:
            return -1
    else:
        return -1
def isNumber(n):
    try:
        eval(n)
    except:
        return False
    else:
        return True
def seperateVariables(term):
    #without operators
    operators=['*','/','^']
    variables=('x','y')
    term=term.replace('**','^')
    parts=[]
    temp=''
    coef=1
    op=[]
    vari=[]
    power=[]
    for i in range(len(term)):
        if term[i] not in variables:
            temp+=term[i]
        else:
            vari.append(term[i])
            parts.append(temp)
            temp=''
    if len(temp)>0:
        parts.append(temp)
    if parts[0]!='':
        if '*'== parts[0][-1] or '/'== parts[0][-1]:
            coef=parts[0][:-1]
            op.append(parts[0][-1])
        else:
            coef=parts[0]
            op.append('*')
    else:
        coef='1'
        op.append('*')
    parts.pop(0)
    for part in parts:
        if '*'== part[-1] or '/' ==part[-1]:
            op.append(part[-1])
            temp=part[1:-1]
            if len(temp)>0:
                power.append(temp)
            else:
                power.append('1')
        else:
            temp=part[1:]
            if len(temp)>0:
                power.append(temp)
            else:
                power.append('1')
    if len(vari)==0:
        op=[]
    return coef,op,vari,power









def biTermExpansion(a,operator,b,n):
    coef_a,op_a,vari_a,power_a=seperateVariables(a)
    coef_b,op_b,vari_b,power_b=seperateVariables(b)
    if not isNumber(coef_a) or not isNumber(coef_b):
        return False,'Invalid a or b'
    for x in power_a:
        if not isNumber(x):
            return False,'Invalid power in a'
    for x in power_b:
        if not isNumber(x):
            return False,'Invalid power in b'
    if not isInteger(n):
        return False,'Invalid n'
    if operator!='+' and operator!='-':
        return False,'Invalid operator'
    output=''
    n=int(n)
    for r in range(n+1):
        print(n,r,ncr(n,r))
        coefficient=str(ncr(n,r))+'*('+coef_a+')**'+str(n-r)+'*('+coef_b+')**'+str(r)
        coefficient=round(eval(coefficient),3)
        print(r,coefficient)

        if 'x' not in vari_a:
            vari_a.append('x')
            power_a.append('0')
            op_a.append('*')
        if 'x' not in vari_b:
            vari_b.append('x')
            power_b.append('0')
            op_b.append('*')
        if 'y' not in vari_a:
            vari_a.append('y')
            power_a.append('0')
            op_a.append('*')
        if 'y' not in vari_b:
            vari_b.append('y')
            power_b.append('0')
            op_b.append('*')

        for i in range(len(op_a)):
            index_b=vari_b.index(vari_a[i])
            op_from_a=True
            if op_a[i]==op_b[index_b]:
                temp_power=eval(power_a[i]+'*'+str(n-r)+'+'+power_b[index_b]+'*'+str(r))
            else:
                temp_power_a=eval(power_a[i]+'*'+str(n-r))
                temp_power_b=eval(power_b[index_b]+'*'+str(r))
                total=temp_power_a-temp_power_b
                if total>=0:
                    temp_power=total
                else:
                    temp_power=-total
                    op_from_a=False

            if coefficient!=1 and i==0:
                output+=str(coefficient)
            if temp_power==0:
                pass
            elif temp_power==1:
                if i==0:#use coefficient=1 only one time in a term
                    if coefficient!=1:
                        if op_from_a:
                            output+=op_a[i]+'('+vari_a[i]+')'
                        else:
                            output+=op_b[index_b]+'('+vari_b[index_b]+')'
                    else:
                        if op_from_a:
                            output+='('+vari_a[i]+')'
                        else:
                            output+='('+vari_b[index_b]+')'
                else:
                    if op_from_a:
                        output+=op_a[i]+'('+vari_a[i]+')'
                    else:
                        output+=op_b[index_b]+'('+vari_b[index_b]+')'
            else:
                if i==0:#use coefficient=1 only one time in a term
                    if coefficient!=1:
                        if op_from_a:
                            output+=op_a[i]+'('+vari_a[i]+'^'+str(temp_power)+')'
                        else:
                            output+=op_b[index_b]+'('+vari_b[index_b]+'^'+str(temp_power)+')'
                    else:
                        if op_from_a:
                            output+='('+vari_a[i]+'^'+str(temp_power)+')'
                        else:
                            output+='('+vari_b[index_b]+'^'+str(temp_power)+')'
                else:
                    if op_from_a:
                        output+=op_a[i]+'('+vari_a[i]+'^'+str(temp_power)+')'
                    else:
                        output+=op_b[index_b]+'('+vari_b[index_b]+'^'+str(temp_power)+')'


        if operator=='+':
            output+=' + '
        else:
            if r%2==0:
                output+=' - '
            else:
                output+=' + '

    output=output.replace('+ +','+')
    output=output.replace('+ -','-')
    output=output.replace('- +','-')
    output=output.replace('- -','+')
    output=output[:-2]

    try:
        return True,round(eval(output),2)
    except:
        return True,output


def quadraticEquations(a,b,c):
    if not(isNumber(a) and isNumber(b) and isNumber(c)):
        return False,'Invalid inputs'
    a=float(a)
    b=float(b)
    c=float(c)
    if a==0:
        return False,'Not a Quadratic Equation'
    x1=(-b+(b**2-4*a*c)**0.5)/(2*a)
    x2=(-b-(b**2-4*a*c)**0.5)/(2*a)
    if x1==x2:
        x1=round(x1,2)
        x2=round(x2,2)
        return True,[x1,x2],'Real and Equal\nx1+x2 = '+str(x1+x2)+'\nx1*x2 = '+str(x1*x2)
    elif x1.imag==0:
        x1=round(x1,2)
        x2=round(x2,2)
        return True,[x1,x2],'Real and Not Equal\nx1+x2 = '+str(x1+x2)+'\nx1*x2 = '+str(x1*x2)
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
        return True,[x1,x2],'Complex\nx1+x2 = '+str(alfa)+'\nx1*x2 = '+str(beta)
    
    
            
    

            
                
            

        
            
    
    
    
        
    
            
            
