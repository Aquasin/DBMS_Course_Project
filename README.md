# DBMS_Course_Project

Repo for DBMS Course Project

## To Run the Project

1. Compile ` FellowshipDatabase.java` using javac
2. Run `FellowshipDatabase` using java

## Thing to do

1. Give better errors comments

2. Look into Optimizing the code. Make the code modular

3. Use StringBuilder if possible for String concatenation instead of +=

## While running commands keep spaces before and after ( and , and only before for );

<h3 style="color:#00ff00">Correct Way</h3>
 
```sql
INSERT INTO TABLE_NAME VALUES ( 'hello' , 20 );
```
<h3 style="color:#ff0000">Wrong Way</h3>

```sql
INSERT INTO TABLE_NAME VALUES ('hello' , 20 );
```

<p style="text-align:center">OR</p>

```sql
INSERT INTO TABLE_NAME VALUES ( 'hello', 20 );
```

<p style="text-align:center">OR</p>

```sql
INSERT INTO TABLE_NAME VALUES ( 'hello' , 20);
```

## COMMANDS:

<ul>
<li>
CREATE (DONE)
</li>
<li>
SELECT (But do refactoring as code is duplicate in multiple places)
</li>
<li>
INSERT (DONE)
</li>
<li>
UPDATE (But do refactoring as code is duplicate in multiple places)
</li>
<li>
DELETE (But do refactoring as code is duplicate in multiple places)
</li>
<li>
DROP (DONE)
</li>
<li>
HELP (DONE)
</li>
<li>
QUIT (DONE)
</li>
<li>
CLEAR (DONE)
</li>
</ul>

## Things to do for extra marks:

Ask Ma'am

<ul>
<li>
ALTER Command 
<br/>
Add and Drop Column 
</li>
<li>
Constraints like Unique, PK, Not Null and Check
<li>
Multiple conditions for select (AND,OR)
<br/>
e.g SELECT * FROM CUSTOMER WHERE ID < 2 OR NAME = XYZ AND SAL < 30000;
</li>
</ul>
