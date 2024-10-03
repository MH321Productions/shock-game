#include <iostream>

#include <boost/asio/ip/tcp.hpp>
#include <nlohmann/json.hpp>

using namespace std;
using json = nlohmann::json;
using sockstream = boost::asio::ip::tcp::iostream;

int main() {
    cout << "Hello World" << endl;

    sockstream stream("127.0.0.1", "8080");
    if (!stream) {
        cerr << "Couldn't connect: " << stream.error().message() << endl;
        return 1;
    }

    cout << "Connected to server" << endl;

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
    stream << first.dump() << endl;

    cout << "Sending second" << endl;
    stream << second.dump() << endl;

    //Read data
    json readFirst;
    json readSecond;
    json readData;

    cout << "Reading first" << endl;
    stream >> readFirst;

    cout << "Reading second" << endl;
    stream >> readSecond;

    cout << "Reading data" << endl;
    stream >> readData;

    cout << "First: " << readFirst.dump(4) << endl << readFirst << endl << endl;
    cout << "Second: " << readSecond.dump(4) << endl << readSecond.dump() << endl;
    cout << "Data: " << readData.dump(4) << endl;

    cout << "Disconnecting" << endl;
    stream.close();
}