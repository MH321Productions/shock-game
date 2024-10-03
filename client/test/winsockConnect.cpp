#include <iostream>
#include <winsock2.h>
#include <Windows.h>
#include <ws2tcpip.h>
#include <unistd.h>
#include <ext/stdio_filebuf.h>

using namespace std;

int main() {
    cout << "Test" << endl;

    //Start Winsock
    WSADATA wsaData;
    int res;

    res = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (res != 0) {
        cerr << "Coudln't startup Winsock: " << res << endl;
        return 1;
    }

    //Create Socket
    struct addrinfo *result = nullptr, *ptr = nullptr, hints {};

    ZeroMemory(&hints, sizeof hints);
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;

    res = getaddrinfo("localhost", "8080", &hints, &result);
    if (res != 0) {
        cerr << "Couldn't get address: " << res;
        WSACleanup();
        return 1;
    }

    SOCKET connectSocket = INVALID_SOCKET;
    ptr = result;
    connectSocket = socket(ptr->ai_family, ptr->ai_socktype, ptr->ai_protocol);
    if (connectSocket == INVALID_SOCKET) {
        cerr << "Couldn't create socket: " << WSAGetLastError() << endl;
        freeaddrinfo(result);
        WSACleanup();
        return 1;
    }

    res = connect(connectSocket, ptr->ai_addr, (int) ptr->ai_addrlen);
    if (res == SOCKET_ERROR) {
        closesocket(connectSocket);
        connectSocket = INVALID_SOCKET;
    }

    freeaddrinfo(result);

    if (connectSocket == INVALID_SOCKET) {
        cerr << "Couldn't connect to server" << endl;
        WSACleanup();
        return 1;
    }

    cout << "Connected to server" << endl;
    __gnu_cxx::stdio_filebuf<char> buf;

    //Send data
    string first = R"({"name": "Hi"})";
    send(connectSocket, first.c_str(), (int) first.size(), 0);

    int fd = _open_osfhandle((intptr_t) connectSocket, 0);
    if (fd == -1) cerr << "Couldn't create fd" << endl;
    else {
        buf = __gnu_cxx::stdio_filebuf<char>(fd, ios::in | ios::out);
        iostream stream(&buf);

        string second = R"({"name": "Heyho"})";
        stream << second << endl << flush;

        //buf.close();
    }

    //Shutdown client sending
    res = shutdown(connectSocket, SD_SEND);
    if (res == SOCKET_ERROR) {
        cerr << "Couldn't shutdown client sending: " << WSAGetLastError() << endl;
        closesocket(connectSocket);
        WSACleanup();
        return 1;
    }

    //Cleanup
    closesocket(connectSocket);
    WSACleanup();

    return 0;

    /*HANDLE fileHandle = CreateFile(
        "test.txt", GENERIC_WRITE,
        0, nullptr, CREATE_ALWAYS,
        FILE_ATTRIBUTE_NORMAL, nullptr
    );

    if (fileHandle != INVALID_HANDLE_VALUE) {
        int fd = _open_osfhandle((intptr_t) fileHandle, 0);

        if (fd != -1) {
            __gnu_cxx::stdio_filebuf<char> buf(fd, ios::out);

            ostream stream(&buf);

            stream << "Test" << endl;

            buf.close();
            CloseHandle(fileHandle);

            fd = -1;
            fileHandle = INVALID_HANDLE_VALUE;
        }
    }*/
}