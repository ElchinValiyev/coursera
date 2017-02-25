#include <iostream>
#include <string>

using namespace std;

// 3rd grade multiplication method, O(n^2)
string multiply(string x, string y);

// implementation of Karatsuba algorithm, O(n^(lg3))
string karatsuba(string x, string y);

//add big numbers
string add(string x, string y);

// subtract big numbers
string subtract(string x, string y);


void sort(int array[], int size);

void mergeSort(int array[], int aux[], int low, int high);

void merge(int array[], int aux[], int low, int mid, int high);



//******************************* Multiplication implementations ***********************************

string multiply(string x, string y) {
    string result(x.length() + y.length(), 0);
    string first, second;

    // first should be the longest number
    if (x.length() > y.length()) {
        first = x;
        second = y;
    } else {
        first = y;
        second = x;
    }

    for (long i = second.length() - 1; i >= 0; i--) {
        int remainder = 0;

        for (long j = first.length() - 1; j >= 0; j--) {
            long index = i + j + 1;

            result[index] += (second[i] - '0') * (first[j] - '0') + remainder;
            remainder = result[index] / 10;
            result[index] %= 10;
        }

        if (remainder > 0)
            result[i] += remainder;
    }

    //turn result from numbers to readable characters
    for (int i = 0; i < result.length(); i++)
        result[i] += '0';
    if (result[0] == '0')
        result = result.substr(1);

    return result;
}

string add(string x, string y) {
    string result(max(x.length(), y.length()) + 1, 0);
    long i = x.length() - 1;
    long j = y.length() - 1;
    long k = result.length() - 1;

    int remainder = 0;
    while (i >= 0 && j >= 0) {
        result[k] += (x[i--] - '0') + (y[j--] - '0') + remainder;
        remainder = result[k] / 10;
        result[k] %= 10;
        k--;
    }

    while (i >= 0) {
        result[k] += (x[i--] - '0') + remainder;
        remainder = result[k] / 10;
        result[k--] %= 10;
    }

    while (j >= 0) {
        result[k] += (y[j--] - '0') + remainder;
        remainder = result[k] / 10;
        result[k--] %= 10;
    }

    if (remainder != 0)
        result[0] += remainder;


    //turn result from numbers to readable characters
    for (int i = 0; i < result.length(); i++)
        result[i] += '0';
    //removing leading zero
    if (result[0] == '0')
        result = result.substr(1);

    return result;
}

string subtract(string x, string y) {
    string result = x.substr(0);

    for (int i = 0; i < result.length(); i++)
        result[i] -= '0';
    for (int i = 0; i < y.length(); i++)
        y[i] -= '0';

    long i = result.length() - y.length(), j = 0;

    while (j < y.length()) {
        if (result[i] < y[j]) {
            result[i - 1] -= 1;
            result[i] += 10;
        }
        result[i++] -= y[j++];
    }

    //turn result from numbers to readable characters
    for (int i = 0; i < result.length(); i++)
        result[i] += '0';
    //removing leading zero
    if (result[0] == '0')
        result = result.substr(1);
    return result;
}

string karatsuba(string x, string y) {

    if (x.length() == 1 || y.length() == 1)
        return multiply(x, y);


    string xHigh = x.substr(0, x.length() / 2);
    string xLow = x.substr(x.length() / 2);

    string yHigh = y.substr(0, y.length() / 2);
    string yLow = y.substr(y.length() / 2);

    string a = karatsuba(xHigh, yHigh);
    string b = karatsuba(xLow, yLow);

    string c = karatsuba(add(xHigh, xLow), add(yHigh, yLow));
    c = subtract(c, add(a, b));

    return add(add(a + string(x.length(), '0'), c + string(x.length() / 2, '0')), b);
}


//********************************** MergeSort implementation **************************

void sort(int array[], int size) {
    int aux[size]; // auxiliary array, to store copies
    mergeSort(array, aux, 0, size - 1);
}

void mergeSort(int array[], int aux[], int low, int high) {
    if (high == low) return;
    int middle = (high + low) / 2;
    mergeSort(array, aux, low, middle);
    mergeSort(array, aux, middle + 1, high);

    // if last element of left sorted part is smaller or equal of right part,
    // then the whole array is sorted, no merge is needed
    if (array[middle] > array[middle + 1])
        merge(array, aux, low, middle, high);
}

void merge(int array[], int aux[], int low, int mid, int high) {
    int i = low, j = mid + 1, k = low;

    while ((i <= mid) && (j <= high)) {
        if (array[i] <= array[j])
            aux[k++] = array[i++];
        else
            aux[k++] = array[j++];
    }

    while (i <= mid) aux[k++] = array[i++];
    while (j <= high) aux[k++] = array[j++];

    for (k = low; k <= high; k++) array[k] = aux[k];
}


void printArray(int a[], int n) {
    for (int i = 0; i < n; i++)
        cout << a[i] << " ";
}

int main() {

    cout << "Karatsuba result of 1234*4321 = ";
    cout << karatsuba("1234", "4321") << endl;

    int array[] = {0, 6, 8, 4, 5, 2, 1, 3, 7, 9};

    cout << "\nUnsorted order ==> ";
    printArray(array, 10);

    cout << "\nSorted order ==> ";
    sort(array, 10);
    printArray(array, 10);

    return 0;
}