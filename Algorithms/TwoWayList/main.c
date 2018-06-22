#include <stdio.h>
#include <malloc.h>
#include <stdlib.h>
#include <time.h>


typedef struct Node {
    int value;
    struct Node *next;
    struct Node *prev;
} Node;


typedef struct List {
    int size;
    Node *head;
} List;


List* createList() {
    List *tmp = (List*) malloc(sizeof(List));
    tmp->size = 0;
    tmp->head = NULL;
    return tmp;
}


void deleteList(List **list) {
    Node *tmp = (*list)->head;
    Node *next = NULL;
    int n = 0;
    while (n<(*list)->size) {
        next = tmp->next;
        free(tmp);
        tmp = next;
        n++;
    }
    free(*list);
    (*list) = NULL;

}

void pushFront(List *list,int data) {
    Node *tmp = (Node*) malloc(sizeof(Node));
    tmp->value = data;
    if (list->size == 0) {
        list->head = tmp;
        tmp->next = tmp->prev = tmp;
    }else {
        tmp->next = list->head;
        tmp->prev = list->head->prev;
        list->head = tmp;
        list->head->next->prev = tmp;
        list->head->prev->next = tmp;
    }
    list->size++;
}

void pushBack(List *list, int data){
    Node *tmp = (Node*) malloc(sizeof(Node));
    tmp->value = data;
    if (list->size == 0) {
        list->head = tmp;
        tmp->next = tmp->prev = tmp;
    }else {
        tmp->next = list->head;
        tmp->prev = list->head->prev;
        list->head->prev->next = tmp;
        list->head->prev = tmp;
    }
    list->size++;
}

void popFront(List *list){
    if (list->size == 0) {
        deleteList(list);
        return;
    }
    Node *tmp = list->head;
    list->head->prev->next = list->head->next;
    list->head->next->prev = list->head->prev;
    list->head = list->head->next;
    free(tmp);
    list->size--;
}

void popBack(List *list){
    if (list->size == 0) {
        deleteList(list);
        return;
    }
    Node *tmp = list->head->prev;
    list->head->prev = list->head->prev->prev;
    list->head->prev->next = list->head;
    list->size--;
    free(tmp);
}

void pushNth(List *list, int position, int data){
    Node *tmp = (Node*) malloc(sizeof(Node));
    Node *head = list->head;
    int n=0;
    if (list->size<position){
        printf("Nope");
        return ;
    }
    tmp->value=data;
    if(position==0){
        pushFront(list, data);
        return;
    }
    while(n<position){
        head=head->next;
        n++;
    }
    head->prev->next=tmp;
    tmp->next=head;
    tmp->prev=head->prev;
    head->prev=tmp;
    list->size++;
}

void deleteNth(List *list, int position){
    if (list->size<position){
        printf("Nope");
        return ;
    }
    Node *tmp = list->head;
    int n=0;
    while(n<position){
        n++;
        tmp=tmp->next;
    }
    tmp->prev->next=tmp->next;
    tmp->next->prev=tmp->prev;
    free(tmp);
    list->size--;
}

int whereIsN(List *list, int value){
    if (list->size==0){
        printf("Nope");
        return -1;
    }
    Node *tmp = list->head;
    int n = 0;
    while (n<list->size){
        if (tmp->value==value){
            return n;
        }
        tmp=tmp->next;
        n++;
    }
    return -1;
}

int getN(List *list, int position){
    if (list->size==0){
        printf("Nope");
        return -1;
    }
    if (list->size<position)
        return -1;
    Node *tmp = list->head;
    int n=0;
    if (position<=(list->size)/2){
        while (n<position){
            tmp=tmp->next;
            n++;
        }
        return tmp->value;
    }else{
        while ((n+list->size)>position){
            tmp=tmp->prev;
            n--;
        }
        return tmp->value;
    }
}

void printList(List *list) {
    Node *tmp = list->head;
    int n = 0;
    while (n<list->size) {
        printf("%d ", tmp->value);
        tmp = tmp->next;
        n++;
    }
    printf("\n");
}

double timeToN(List *list, int a){
    clock_t begin = clock();
    for (int i=0; i<1000000; i++){
        getN(list, a);
    }
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    return time_spent;
}

double timeToRandom(List *list, int a[]){
    clock_t begin = clock();
    for (int i=0; i<1000; i++){
        for (int k=0; k<1000; k++) {
            getN(list, a[i]);
        }
    }
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    return time_spent;
}

List merge(List *list2, List *list1){
    int c=0;
    int n = list1->size;
    Node *tmp = list1->head;
    while (c<n) {
        pushBack(list2, tmp->value);
        tmp = tmp->next;
        c++;
    }
    return *list2;
}

int main() {
    srand(time(NULL));
    List *list = createList();
    List *list2 = createList();
    int ArrR[1000];
    int a = rand()%1000;
    for (int i=0; i<1000; i++){
        pushFront(list, rand()%10000);
        ArrR[i] = rand()%list->size;
    }
    printf ("to element %d  1m times get %f seconds\n", a, timeToN(list, a));
    printf ("to element %d  1m times get %f seconds\n", list->size-a, timeToN(list, list->size-a));
    printf ("to random element 1m times get %f seconds\n", timeToRandom(list, ArrR));

}
