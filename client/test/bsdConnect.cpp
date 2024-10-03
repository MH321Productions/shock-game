#include <iostream>
#include <string>
#include <cstring>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <ext/stdio_filebuf.h>

#include <nlohmann/json.hpp>

using namespace std;
using json = nlohmann::json;

int main() {
    struct sockaddr_in sa {};
    int SocketFD;

    SocketFD = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (SocketFD == -1) {
        perror("Couldn't create socket");
        return 1;
    }

    memset(&sa, 0, sizeof sa);

    sa.sin_family = AF_INET;
    sa.sin_port = htons(8080);
    inet_pton(AF_INET, "127.0.0.1", &sa.sin_addr);

    if (connect(SocketFD, (struct sockaddr*) &sa, sizeof sa) == -1) {
        perror("Couldn't connect");
        close(SocketFD);
        return 1;
    }

    cout << "Connection established" << endl;

    __gnu_cxx::stdio_filebuf<char> buf(SocketFD, ios::in | ios::out);
    iostream stream(&buf);

    //Write data
    json first = {
        {"name", "Hi"},
        {"age", 69},
        {"hobbies", {"a", "b", "c"}}
    };

    json second = {
        {"name", "Heyho"},
        {"age", 420},
        {"hobbies", {"d", "e", "f"}}
    };

    cout << "Sending first" << endl;
    stream << first.dump() << "\r\n" << flush;

    cout << "Sending second" << endl;
    stream << second.dump() << endl;

    //Read data
    json readFirst;
    json readSecond;

    cout << "Reading first" << endl;
    stream >> readFirst;

    cout << "Reading second" << endl;
    stream >> readSecond;

    cout << "First: " << readFirst.dump(4) << endl << readFirst << endl << endl;
    cout << "Second: " << readSecond.dump(4) << endl << readSecond.dump() << endl;

    close(SocketFD);

    return 0;
}