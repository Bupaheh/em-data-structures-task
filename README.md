# Hard disk performance benchmark
### Command line arguments
```
-s [VALUE], --size [VALUE]
	The number of MiB for a writing benchmark
	
-f [FILE], --file [FILE]
	File path for a reading benchmark
	
-d [FILE], --dir [FILE]
	The directory from which a random file for 
	a reading benchmark will be selected
	
-l [VALUE], --lower [VALUE]
	The lower bound of MiB for a random file
	
-u [VALUE], --upper [VALUE]
	The upper bound of MiB for a random file
	
-o [FILE], --output [FILE]
	Output file
```



### Example

```bash
$ gradle run --args='--dir /var --size 1024'
Path: /var/cache/apt/archives/texlive-luatex_2019.20200218-1_all.deb
Size:  10.00 MiB
Reading speed: 769.50 MiB/s

Size:  1024.00 MiB
Writing speed: 111.74 MiB/s
```