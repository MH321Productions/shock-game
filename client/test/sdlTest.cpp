#include <iostream>

#include <SDL2/SDL_main.h>
#include <SDL2/SDL.h>

using namespace std;

int main(int argc, char* argv[]) {
    if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
        cerr << "Couldn't init SDL: " << SDL_GetError() << endl;
        return 1;
    }

    SDL_Window* window = SDL_CreateWindow(
        "SDL Test",
        SDL_WINDOWPOS_CENTERED,
        SDL_WINDOWPOS_CENTERED,
        1280, 720,
        SDL_WINDOW_ALLOW_HIGHDPI | SDL_WINDOW_RESIZABLE
    );
    if (!window) {
        cerr << "Couldn't create window: " << SDL_GetError() << endl;
        SDL_Quit();
        return 1;
    }

    SDL_Renderer* renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
    if (!renderer) {
        cerr << "Couldn't create renderer: " << SDL_GetError() << endl;
        SDL_Quit();
        return 1;
    }

    SDL_Event event;
    bool running = true;
    SDL_Rect r {100, 100, 50, 100};

    while (running) {
        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_QUIT) running = false;
        }

        SDL_SetRenderDrawColor(renderer, 0, 0, 255, 255);
        SDL_RenderClear(renderer);
        SDL_SetRenderDrawColor(renderer, 255, 0, 0, 255);
        SDL_RenderFillRect(renderer, &r);
        SDL_RenderPresent(renderer);

        SDL_Delay(10);
    }

    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();
    return 0;
}