# design-patterns
Two lab exercises from design patterns course @ FER ZG

Idea is to create simple apps like paint and notepad but in a smooth way so design patterns are implemented.

In JPaintMM are implemented:

Observer - relation between data model and shown objects
Composite - same transparent operations for one and more objects
Iterator - for iterating through elements of paint
Prototype - in toolbar can be added new shapes which do not depend on concrete supported geometric shapes
Factory - creating concrete shapes based on symbolic names when loading a drawing from file saved in native format
State - in different states, moving mouse, keyboard input etc. are differently processed
Bridge - transparently drawing paint and exporting it in different image formats (SVG for example)

In JNotepadMM are implemented:

Observer, Iterator, Factory, Command (JMenuBar), Singleton (only one instance of UndoManager) ..
