#include <malloc.h>
#include <stdbool.h>

typedef struct Node {
    int value;
    struct Node *next;
} Node;

typedef struct List {
    int size;
    Node *head;
    Node *tail;
} List;

List* createList() {
    List *tmp = (List*) malloc(sizeof(List));
    tmp->size = 0;
    tmp->head = NULL;
    tmp->tail = NULL;
    return tmp;
}

void Push(List *list, int data){
    Node *tmp = (Node*) malloc(sizeof(Node));
    tmp->value = data;
    if (list->size == 0) {
        list->head = tmp;
        list->tail = tmp;
    }else {
        list->tail->next = tmp;
        list->tail = tmp;
    }
    list->size++;
}

void Pop(List *list){
    if (list->size==0){ return; }
    Node *tmp = list->head;
    if (list->size==1){
        free(list);
    }
    list->head=list->head->next;
    free(tmp);
    list->size--;
}

bool isEmpty(List *list){
    return(list->size==0);
}

void showFifo(List *list){
    Node *tmp = list->head;
    for (int i = 0; i<list->size; i++){
        printf("%d ", tmp->value);
        tmp = tmp->next;
    }
    printf("\n");
}

void DeleteFifo(List **list){
    Node *tmp = (*list)->head;
    Node *next = NULL;
    int n = 0;
    while (n<(*list)->size) {
        next = tmp->next;
        free(tmp);
        tmp = next;n++;
    }
    free(*list);
    (*list) = NULL;
}

int main(){
    List *fifo = createList();
    printf("%s\n", isEmpty(fifo) ? "true" : "false");
    Push(fifo, 1);
    Push(fifo, 2);
    showFifo(fifo);
    Push(fifo, 3);
    Push(fifo, 4);
    Push(fifo, 5);
    Push(fifo, 6);
    showFifo(fifo);
    Pop(fifo);
    Pop(fifo);
    Push(fifo, -1);
    showFifo(fifo);
    printf("%s\n", isEmpty(fifo) ? "true" : "false");
    DeleteFifo(&fifo);
    fifo = createList();
    showFifo(fifo);
    Push(fifo, 1);
    showFifo(fifo);
}



















