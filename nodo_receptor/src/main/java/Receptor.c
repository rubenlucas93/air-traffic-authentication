#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>


int main(){


pid_t pid;
int tuberia[2];

pipe(tuberia);

pid=fork();

if(pid==0){




close(0);
close(tuberia[1]);
dup(tuberia[0]);

execl("/home/pi/Desktop/nodo_receptor/src/main/java/Decoder.sh", "./Decoder.sh", NULL);

}else{



close(1);
close(tuberia[0]);
dup(tuberia[1]);


execl("/home/pi/Desktop/nodo_receptor/src/main/java/dump1090.sh", "./dump1090.sh", NULL);




}
}
