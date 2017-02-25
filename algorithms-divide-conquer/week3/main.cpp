#include <iostream>
#include <string>
#include <fstream>

using namespace std;

int numberOfCompares;

void quickSort(int a[], int length);

//sort subarray
void sort(int array[], int low, int high);

// returns pivot's final position
int partition(int array[], int low, int high);

//exchange 2 elements of array
void swap(int array[], int i, int j);

void printArray(int array[], int size);

int *readFileIntoArray(string fileName, int numberOfLines);

//************************************ Implementation ***********************************

void quickSort(int array[], int length) {
    numberOfCompares = 0;
    sort(array, 0, length - 1);
}

void sort(int array[], int low, int high) {
    if (low >= high) return;

    int pivot = partition(array, low, high);

    sort(array, low, pivot - 1);
    sort(array, pivot + 1, high);
}

int partition(int array[], int low, int high) {

    int pivot = array[low];
    int firstOpened = low;

    for (int i = low + 1; i <= high; i++) {
        if (array[i] < pivot) {
            firstOpened++;
            swap(array, i, firstOpened);
        }
        numberOfCompares++;
    }
    //moving pivot to it's correct position
    swap(array, low, firstOpened);
    return firstOpened;
}

void swap(int array[], int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
}

void printArray(int array[], int size) {
    cout << "[ ";
    for (int i = 0; i < size; i++)
        cout << array[i] << " ";
    cout << "]" << endl;
}

int *readFileIntoArray(string fileName, int numberOfLines) {
    int *numbers = new int[numberOfLines];
    fstream file;
    file.open(fileName);
    if (file.is_open()) {
        for (int i = 0; i < numberOfLines; i++) {
            file >> numbers[i];
        }
    } else cout << "Error! File is not opened!" << endl;
    return numbers;
}


int main() {
    int a[] = {6, 8, 7, 8, 5, 8, 9, 4, 7, 45, 27, 94, 65, 10, 32, 57};
    int size = sizeof(a) / sizeof(int);

    cout << "Before sorting: ";
    printArray(a, size);

    cout << "After sorting:  ";
    quickSort(a, size);
    printArray(a, size);

    cout << "\nLoading 10000 numbers from file and sorting... \n";
    int *numbersFromFile = readFileIntoArray("/home/elchin/ClionProjects/week3/QuickSort.txt", 10000);
    quickSort(numbersFromFile, 10000);
    cout << "Number of compares: " << numberOfCompares;

    return 0;
}