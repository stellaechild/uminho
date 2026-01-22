export enum FormElementTypes {
    TEXT = 'text',
    FILE = 'file',
    CHECKBOX = 'checkbox',
    TEXTAREA = 'textarea',
    HIDDEN = 'hidden',
}

export interface IFormElement {
    name: string;
    label: string;
    placeholder?: string;
    obfuscated?: boolean;
    value?: string | FileList | boolean;
    type: FormElementTypes;
    required: boolean;
    readonly?: boolean;
}

export interface IButton {
    label: string;
    isFormButton: boolean;
    onClick?: () => void;
    formAction?: string;
    style?: string;
}