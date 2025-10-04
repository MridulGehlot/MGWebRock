class College
{
get()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/acro/get')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
add()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/acro/add')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
m2()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/acro/m2')
.then(response => response.text())
.then(data => {resolve(data);})
.catch(error => console.log(error))
});
}
}
class Customer
{
constructor(name,age)
{
this.name=name;
this.age=age;
}
}

class Student
{
constructor(id,name,gender)
{
this.id=id;
this.name=name;
this.gender=gender;
}
}

class StudentService
{
update(arg0)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/student/update',{'method':'POST','headers':{'Content-Type':'application/json'},'body':JSON.stringify(arg0)})
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
add(arg0)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/student/add',{'method':'POST','headers':{'Content-Type':'application/json'},'body':JSON.stringify(arg0)})
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
delete(arg0)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/student/delete?id='+arg0)
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
getAll()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/student/getAll')
.then(response => {if(!response.ok){throw new Error(`HTTP error! status: ${response.status}`);}return response.json()})
.then(data => {resolve(data);})
.catch(error => console.log(error))
});
}
getById(arg0)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/student/getById?id='+arg0)
.then(response => {if(!response.ok){throw new Error(`HTTP error! status: ${response.status}`);}return response.json()})
.then(data => {resolve(data);})
.catch(error => console.log(error))
});
}
}
class Organization
{
get()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/org/get')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
customDetails(arg0)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/org/customer')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
summ()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/org/summer')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
somemethod(arg0,arg1,arg2)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/org/testing?pqr='+arg0+'&xyz='+arg1)
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
}
class School
{
get()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/gyansagar/get')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
add()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/gyansagar/add',{'method':'POST','headers':{'Content-Type':'application/json'}})
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
m2(arg0,arg1)
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/gyansagar/m2?xyz='+arg0+'&pqr='+arg1)
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
greet()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/gyansagar/greet')
.then(response => {if(!response.ok){throw new Error(`HTTP error! status: ${response.status}`);}return response.json()})
.then(data => {resolve(data);})
.catch(error => console.log(error))
});
}
}
class Stud
{
constructor(rollNumber,name)
{
this.rollNumber=rollNumber;
this.name=name;
}
}

class TestingSecurity
{
doSomething()
{
return new Promise(function(resolve,reject){
fetch('http://localhost:8080/MGWebRock/schoolService/secure/service')
.then(response => response.text())
.then(data => {
if(data!=='OK')
{
reject();
return;
}
resolve();
})
.catch(error => console.log(error))
});
}
}
