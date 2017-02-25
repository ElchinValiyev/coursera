#include <iostream>
#include <fstream>

using namespace std;

// Main function to count inversions
long countInversions(int array[], int n);

//********************************** Count number of inversions ***********************************

long mergeAndCountSplitInversions(int array[], int aux[], int low, int middle, int high) {
    int i = low, j = middle + 1, k = low;
    long numberOfSplits = 0;

    for (int l = low; l <= high; l++)
        aux[l] = array[l];

    while (i <= middle && j <= high) {
        if (aux[i] <= aux[j])
            array[k++] = aux[i++];
        else {
            array[k++] = aux[j++];
            numberOfSplits += middle + 1 - i;
        }
    }

    while (i <= middle) array[k++] = aux[i++];
    while (j <= high) array[k++] = aux[j++];
    return numberOfSplits;
}

long countInversionsRecur(int array[], int aux[], int low, int high) {
    if (low == high) return 0;

    int middle = (low + high) / 2;
    long x = countInversionsRecur(array, aux, low, middle);
    long y = countInversionsRecur(array, aux, middle + 1, high);
    long z = mergeAndCountSplitInversions(array, aux, low, middle, high);

    return x + y + z;
}

long countInversions(int array[], int n) {
    int aux[n];
    return countInversionsRecur(array, aux, 0, n - 1);
}

void printArray(int a[], int n) {
    for (int i = 0; i < n; i++)
        cout << a[i] << " ";
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
    cout << "Number of inversions in array [";
    int array[] = {1, 3, 5, 2, 4, 6};
    printArray(array, 6);
    cout << "] = " << countInversions(array, 6);


    int *integers = readFileIntoArray("/home/elchin/ClionProjects/week2/integers.txt", 100000);
    cout << "\nNumber of inversions in file = " << countInversions(integers, 100000);
    delete[] integers;

    return 0;
}